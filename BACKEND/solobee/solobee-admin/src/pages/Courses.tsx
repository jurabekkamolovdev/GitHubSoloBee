import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { coursesApi, type Category } from '../api/courses';
import { Plus, Trash2, Edit2, Loader2, BookOpen, ChevronRight } from 'lucide-react';

export const Courses = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [isCreating, setIsCreating] = useState(false);
  const [newCat, setNewCat] = useState({ name: '', backgroundColor: '#ffffff', foregroundColor: '#000000', orderIndex: 0 });
  const [editingId, setEditingId] = useState<string | null>(null);

  const fetchCategories = async () => {
    try {
      const data = await coursesApi.getCategories();
      setCategories(data);
    } catch (error) {
      console.error('Failed to load courses', error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleSaveCategory = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newCat.name) return;

    try {
      if (editingId) {
        await coursesApi.updateCategory(editingId, newCat);
      } else {
        await coursesApi.createCategory(newCat);
      }

      setIsCreating(false);
      setEditingId(null);
      setNewCat({ name: '', backgroundColor: '#ffffff', foregroundColor: '#000000', orderIndex: 0 });
      fetchCategories();
    } catch (error) {
      console.error('Failed to save category', error);
      alert('Error saving category');
    }
  };

  const startEdit = (cat: Category, index: number) => {
    setNewCat({
      name: cat.name,
      backgroundColor: cat.backgroundColor,
      foregroundColor: cat.foregroundColor,
      orderIndex: index,
    });
    setEditingId(cat.id);
    setIsCreating(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm('Haqiqatdan ham bu kategoriyani o\\\'chirmoqchimisiz?')) return;
    try {
      await coursesApi.deleteCategory(id);
      fetchCategories();
    } catch (error) {
      console.error('Error deleting category', error);
    }
  };

  if (loading) return <div className="flex justify-center mt-10"><Loader2 className="animate-spin w-8 h-8 text-primary-600" /></div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div>
          <h1 className="text-2xl font-bold tracking-tight text-gray-900">Kuriklum va Darsliklar</h1>
          <p className="text-sm text-gray-500 mt-1">Platformadagi barcha yo'nalishlar (Categoriyalar) ro'yxati</p>
        </div>
        <button
          onClick={() => setIsCreating(!isCreating)}
          className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 transition"
        >
          <Plus className="w-5 h-5 mr-2" />
          Yangi Yaratish
        </button>
      </div>

      {isCreating && (
        <form onSubmit={handleSaveCategory} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 space-y-4">
          <h2 className="text-lg font-semibold border-b pb-2">
            {editingId ? 'Kategoriyani tahrirlash' : 'Yangi Kategoriya qo\'shish'}
          </h2>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 border-b border-gray-100 pb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Nomi</label>
              <input
                type="text"
                value={newCat.name}
                onChange={(e) => setNewCat({ ...newCat, name: e.target.value })}
                className="mt-1 block w-full p-2 border rounded-lg"
                placeholder="Masalan: Yozuv, Ingliz tili"
                required
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Tartib raqami (Order Index)</label>
              <input
                type="number"
                value={newCat.orderIndex}
                onChange={(e) => setNewCat({ ...newCat, orderIndex: parseInt(e.target.value) || 0 })}
                className="mt-1 block w-full p-2 border rounded-lg"
              />
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4 border-b border-gray-100 pb-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Orqa fon rangi (HEX)</label>
              <div className="flex items-center mt-1 space-x-2">
                <input
                  type="color"
                  value={newCat.backgroundColor}
                  onChange={(e) => setNewCat({ ...newCat, backgroundColor: e.target.value })}
                  className="w-10 h-10 border rounded cursor-pointer"
                />
                <input
                  type="text"
                  value={newCat.backgroundColor}
                  onChange={(e) => setNewCat({ ...newCat, backgroundColor: e.target.value })}
                  className="flex-1 p-2 border rounded-lg"
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Shrift rangi (HEX)</label>
              <div className="flex items-center mt-1 space-x-2">
                <input
                  type="color"
                  value={newCat.foregroundColor}
                  onChange={(e) => setNewCat({ ...newCat, foregroundColor: e.target.value })}
                  className="w-10 h-10 border rounded cursor-pointer"
                />
                <input
                  type="text"
                  value={newCat.foregroundColor}
                  onChange={(e) => setNewCat({ ...newCat, foregroundColor: e.target.value })}
                  className="flex-1 p-2 border rounded-lg"
                />
              </div>
            </div>
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={() => {
                setIsCreating(false);
                setEditingId(null);
                setNewCat({ name: '', backgroundColor: '#ffffff', foregroundColor: '#000000', orderIndex: 0 });
              }}
              className="px-4 py-2 border rounded-xl text-gray-600 font-medium"
            >
              Bekor qilish
            </button>
            <button type="submit" className="px-4 py-2 bg-primary-600 text-white rounded-xl flex items-center">
              Saqlash
            </button>
          </div>
        </form>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {categories.map((cat, catIndex) => (
          <div key={cat.id} className="group relative rounded-2xl border p-5 shadow-sm transition-all hover:shadow-md overflow-hidden bg-white">
            <div className="absolute inset-0 opacity-[0.03]" style={{ backgroundColor: cat.backgroundColor }}></div>
            
            <div className="relative flex justify-between items-start">
              <div 
                className="w-16 h-16 rounded-xl flex items-center justify-center shadow-inner overflow-hidden border border-white/20"
                style={{ backgroundColor: cat.backgroundColor }}
              >
                {cat.images?.length > 0 ? (
                  <img src={cat.images[0].url} alt={cat.name} className="w-full h-full object-cover" />
                ) : (
                  <BookOpen className="w-8 h-8 opacity-90" style={{ color: cat.foregroundColor || '#fff' }} />
                )}
              </div>
              
              <div className="space-x-2">
                <button
                  type="button"
                  onClick={(e) => { e.stopPropagation(); startEdit(cat, catIndex); }}
                  className="p-2 text-gray-500 hover:text-blue-600 bg-gray-50 hover:bg-blue-50 rounded-lg transition"
                  aria-label="Tahrirlash"
                >
                  <Edit2 className="w-4 h-4" />
                </button>
                <button
                  type="button"
                  onClick={(e) => { e.stopPropagation(); handleDelete(cat.id); }}
                  className="p-2 text-gray-500 hover:text-red-600 bg-gray-50 hover:bg-red-50 rounded-lg transition"
                  aria-label="O'chirish"
                >
                  <Trash2 className="w-4 h-4" />
                </button>
              </div>
            </div>

            <div className="mt-4 relative">
              <h3 className="font-bold text-gray-900 text-lg">{cat.name}</h3>
              <p className="text-sm text-gray-500 mt-1">{cat.subCategories?.length || 0} ta modullar</p>
            </div>
            
            <div className="mt-5 border-t pt-4 relative">
              <button
                onClick={() => navigate(`/courses/${cat.id}`)}
                className="text-sm font-medium w-full py-2 bg-gray-50 text-gray-700 rounded-lg hover:bg-primary-50 hover:text-primary-700 transition flex items-center justify-center gap-1"
              >
                Modullarni boshqarish
                <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};
