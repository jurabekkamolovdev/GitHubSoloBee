import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { coursesApi, type Category, type SubCategory } from '../api/courses';
import {
  Plus, Trash2, Edit2, Loader2, ArrowLeft, Image as ImageIcon, Layers, ChevronRight,
} from 'lucide-react';

export const CategoryDetail = () => {
  const { categoryId } = useParams<{ categoryId: string }>();
  const navigate = useNavigate();

  const [category, setCategory] = useState<Category | null>(null);
  const [subCategories, setSubCategories] = useState<SubCategory[]>([]);
  const [loading, setLoading] = useState(true);

  // Form state
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [form, setForm] = useState({ name: '', orderIndex: 0 });
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null); // display URL
  const [uploading, setUploading] = useState(false);

  const fetchData = async () => {
    try {
      const categories = await coursesApi.getCategories();
      const cat = categories.find((c) => c.id === categoryId);
      if (!cat) { navigate('/courses'); return; }
      setCategory(cat);
      setSubCategories(cat.subCategories ?? []);
    } catch (err) {
      console.error('Failed to load category', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, [categoryId]);

  // ── Form helpers ─────────────────────────────────────────────

  const openCreate = () => {
    setEditingId(null);
    setForm({ name: '', orderIndex: subCategories.length });
    setSelectedFile(null);
    setPreviewUrl(null);
    setIsFormOpen(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const openEdit = (sub: SubCategory, index: number) => {
    setEditingId(sub.id);
    setForm({ name: sub.name, orderIndex: index });
    setSelectedFile(null);
    setPreviewUrl(sub.thumbnailUrl ?? null);
    setIsFormOpen(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const closeForm = () => {
    setIsFormOpen(false);
    setEditingId(null);
    setForm({ name: '', orderIndex: 0 });
    setSelectedFile(null);
    setPreviewUrl(null);
  };

  const handleFileSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    setSelectedFile(file);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!form.name.trim()) return;

    try {
      setUploading(true);

      let newThumbnailKey: string | null | undefined;

      if (selectedFile) {
        const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(selectedFile, 'subcategories');
        newThumbnailKey = fileKey;
        setPreviewUrl(publicUrl);
      } else if (!previewUrl) {
        newThumbnailKey = null;
      }

      if (editingId) {
        await coursesApi.updateSubCategory(editingId, {
          name: form.name,
          orderIndex: form.orderIndex,
          ...(newThumbnailKey !== undefined ? { thumbnailKey: newThumbnailKey } : {}),
        });
      } else {
        await coursesApi.createSubCategory({
          name: form.name,
          categoryId: categoryId!,
          orderIndex: form.orderIndex,
          ...(newThumbnailKey ? { thumbnailKey: newThumbnailKey } : {}),
        });
      }

      closeForm();
      fetchData();
    } catch (err) {
      console.error('Failed to save sub-category', err);
      alert('Saqlashda xatolik yuz berdi');
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (sub: SubCategory) => {
    if (!window.confirm(`"${sub.name}" modulni o'chirmoqchimisiz? Unga tegishli barcha topiclar ham o'chadi.`)) return;
    try {
      await coursesApi.deleteSubCategory(sub.id);
      fetchData();
    } catch (err) {
      console.error('Delete failed', err);
    }
  };

  // ── Render ───────────────────────────────────────────────────

  if (loading) {
    return (
      <div className="flex justify-center mt-10">
        <Loader2 className="animate-spin w-8 h-8 text-primary-600" />
      </div>
    );
  }

  return (
    <div className="space-y-6">

      {/* Header */}
      <div className="flex items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex items-center gap-4">
          <button
            onClick={() => navigate('/courses')}
            className="p-2 rounded-lg hover:bg-gray-100 text-gray-500 hover:text-gray-900 transition"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2 text-sm text-gray-400 mb-0.5">
              <span
                className="cursor-pointer hover:text-primary-600 transition"
                onClick={() => navigate('/courses')}
              >
                Kurrikulum
              </span>
              <span>/</span>
              <span className="text-gray-700 font-medium">{category?.name}</span>
            </div>
            <h1 className="text-2xl font-bold text-gray-900">{category?.name} — Modullar</h1>
          </div>
        </div>

        <button
          onClick={openCreate}
          className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 transition"
        >
          <Plus className="w-5 h-5 mr-2" />
          Yangi Modul
        </button>
      </div>

      {/* Form */}
      {isFormOpen && (
        <form
          onSubmit={handleSave}
          className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 space-y-5"
        >
          <h2 className="text-lg font-semibold border-b pb-3">
            {editingId ? 'Modulni tahrirlash' : 'Yangi Modul qo\'shish'}
          </h2>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {/* Name */}
            <div>
              <label className="block text-sm font-medium text-gray-700">
                Nomi <span className="text-red-500">*</span>
              </label>
              <input
                type="text"
                value={form.name}
                onChange={(e) => setForm({ ...form, name: e.target.value })}
                className="mt-1 block w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                placeholder="Masalan: Alphabet, Sonlar, Mevalar"
                required
              />
            </div>

            {/* Order index */}
            <div>
              <label className="block text-sm font-medium text-gray-700">Tartib raqami</label>
              <input
                type="number"
                value={form.orderIndex}
                onChange={(e) => setForm({ ...form, orderIndex: parseInt(e.target.value) || 0 })}
                className="mt-1 block w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
                min={0}
              />
            </div>
          </div>

          {/* Thumbnail */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Muqova rasmi</label>

            {(previewUrl || selectedFile) ? (
              <div className="flex items-start gap-4">
                <div className="relative w-32 h-32 rounded-xl overflow-hidden border border-gray-200 flex-shrink-0">
                  <img
                    src={selectedFile ? URL.createObjectURL(selectedFile) : previewUrl!}
                    alt="preview"
                    className="w-full h-full object-cover"
                  />
                  <button
                    type="button"
                    onClick={() => { setSelectedFile(null); setPreviewUrl(null); }}
                    className="absolute top-1 right-1 bg-red-500 text-white p-1 rounded-full hover:bg-red-600 transition"
                  >
                    <Trash2 className="w-3 h-3" />
                  </button>
                </div>
                <label className="flex items-center gap-2 px-4 py-2 border border-dashed rounded-xl cursor-pointer hover:border-primary-500 hover:bg-primary-50 transition text-sm text-gray-500">
                  <ImageIcon className="w-4 h-4" />
                  Rasmni almashtirish
                  <input type="file" className="sr-only" onChange={handleFileSelect} accept="image/*" />
                </label>
              </div>
            ) : (
              <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed rounded-xl cursor-pointer hover:border-primary-500 hover:bg-primary-50 transition text-gray-400">
                <ImageIcon className="w-8 h-8 mb-1" />
                <span className="text-sm">Rasm tanlash</span>
                <input type="file" className="sr-only" onChange={handleFileSelect} accept="image/*" />
              </label>
            )}
          </div>

          {/* Actions */}
          <div className="flex justify-end gap-3 pt-2 border-t">
            <button
              type="button"
              onClick={closeForm}
              className="px-4 py-2 border rounded-xl text-gray-600 font-medium hover:bg-gray-50 transition"
            >
              Bekor qilish
            </button>
            <button
              type="submit"
              disabled={uploading}
              className="px-4 py-2 bg-primary-600 text-white rounded-xl flex items-center gap-2 hover:bg-primary-700 transition disabled:opacity-60"
            >
              {uploading && <Loader2 className="w-4 h-4 animate-spin" />}
              {uploading ? 'Saqlanmoqda...' : 'Saqlash'}
            </button>
          </div>
        </form>
      )}

      {/* SubCategory grid */}
      {subCategories.length === 0 && !isFormOpen ? (
        <div className="bg-white rounded-2xl border border-dashed border-gray-200 p-12 text-center">
          <Layers className="w-12 h-12 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-500 font-medium">Hali modul qo'shilmagan</p>
          <p className="text-sm text-gray-400 mt-1">Yuqoridagi "Yangi Modul" tugmasini bosing</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
          {subCategories.map((sub, subIndex) => (
            <div
              key={sub.id}
              className="group bg-white rounded-2xl border border-gray-100 shadow-sm hover:shadow-md transition-all overflow-hidden"
            >
              {/* Thumbnail */}
              <div className="h-36 bg-gray-50 flex items-center justify-center overflow-hidden">
                {sub.thumbnailUrl ? (
                  <img
                    src={sub.thumbnailUrl}
                    alt={sub.name}
                    className="w-full h-full object-cover"
                  />
                ) : (
                  <Layers className="w-12 h-12 text-gray-200" />
                )}
              </div>

              {/* Info + actions */}
              <div className="p-4">
                <div className="flex items-center justify-between mb-3">
                  <div>
                    <h3 className="font-semibold text-gray-900">{sub.name}</h3>
                    <p className="text-xs text-gray-400 mt-0.5">
                      {sub.topics?.length ?? 0} ta topic · tartib: {subIndex + 1}
                    </p>
                  </div>
                  <div className="flex gap-1">
                    <button
                      type="button"
                      onClick={(e) => { e.stopPropagation(); openEdit(sub, subIndex); }}
                      className="p-2 text-gray-500 hover:text-blue-600 bg-gray-50 hover:bg-blue-50 rounded-lg transition"
                      aria-label="Tahrirlash"
                    >
                      <Edit2 className="w-4 h-4" />
                    </button>
                    <button
                      type="button"
                      onClick={(e) => { e.stopPropagation(); handleDelete(sub); }}
                      className="p-2 text-gray-500 hover:text-red-600 bg-gray-50 hover:bg-red-50 rounded-lg transition"
                      aria-label="O'chirish"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </div>
                <button
                  onClick={() => navigate(`/courses/${categoryId}/${sub.id}`)}
                  className="text-sm font-medium w-full py-2 bg-gray-50 text-gray-700 rounded-lg hover:bg-primary-50 hover:text-primary-700 transition flex items-center justify-center gap-1"
                >
                  Topiclarni boshqarish
                  <ChevronRight className="w-4 h-4" />
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
