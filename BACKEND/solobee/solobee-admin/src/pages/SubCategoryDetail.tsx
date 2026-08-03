import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { coursesApi, type Topic, type SubCategory } from '../api/courses';
import { Plus, Trash2, Edit2, Loader2, ArrowLeft, Image as ImageIcon, BookOpen, ChevronRight, GripVertical } from 'lucide-react';
import {
  DndContext,
  closestCenter,
  PointerSensor,
  useSensor,
  useSensors,
  type DragEndEvent,
} from '@dnd-kit/core';
import {
  SortableContext,
  arrayMove,
  rectSortingStrategy,
  useSortable,
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';

export const SubCategoryDetail = () => {
  const { categoryId, subCategoryId } = useParams<{ categoryId: string; subCategoryId: string }>();
  const navigate = useNavigate();

  const [subCategory, setSubCategory] = useState<SubCategory | null>(null);
  const [topics, setTopics] = useState<Topic[]>([]);
  const [loading, setLoading] = useState(true);

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);
  const [savingOrder, setSavingOrder] = useState(false);

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 6 } }),
  );

  const fetchData = async () => {
    try {
      const [sub, topicList] = await Promise.all([
        coursesApi.getSubCategory(subCategoryId!),
        coursesApi.getTopicsBySubCategory(subCategoryId!),
      ]);
      setSubCategory(sub);
      setTopics(topicList ?? []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchData(); }, [subCategoryId]);

  const openCreate = () => {
    setEditingId(null);
    setSelectedFile(null);
    setPreviewUrl(null);
    setIsFormOpen(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const openEdit = (topic: Topic) => {
    setEditingId(topic.id);
    setSelectedFile(null);
    setPreviewUrl(topic.thumbnailUrl ?? null);
    setIsFormOpen(true);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  const closeForm = () => {
    setIsFormOpen(false);
    setEditingId(null);
    setSelectedFile(null);
    setPreviewUrl(null);
  };

  const handleSave = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      setUploading(true);
      let newThumbnailKey: string | null | undefined;
      if (selectedFile) {
        const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(selectedFile, 'topics');
        newThumbnailKey = fileKey;
        setPreviewUrl(publicUrl);
      } else if (!previewUrl) {
        newThumbnailKey = null;
      }
      const thumbnailPatch = newThumbnailKey !== undefined ? { thumbnailKey: newThumbnailKey } : {};
      if (editingId) {
        await coursesApi.updateTopic(editingId, { ...thumbnailPatch });
      } else {
        await coursesApi.createTopic({ subCategoryId: subCategoryId!, orderIndex: topics.length, ...thumbnailPatch });
      }
      closeForm();
      fetchData();
    } catch (err) {
      console.error(err);
      alert('Saqlashda xatolik');
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (topic: Topic) => {
    if (!window.confirm(`Topicni o'chirmoqchimisiz?`)) return;
    try {
      await coursesApi.deleteTopic(topic.id);
      fetchData();
    } catch (err) {
      console.error(err);
    }
  };

  const handleDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event;
    if (!over || active.id === over.id) return;

    const oldIndex = topics.findIndex(t => t.id === active.id);
    const newIndex = topics.findIndex(t => t.id === over.id);
    if (oldIndex < 0 || newIndex < 0) return;

    const previous = topics;
    const reordered = arrayMove(topics, oldIndex, newIndex);
    setTopics(reordered);

    try {
      setSavingOrder(true);
      await coursesApi.reorderTopics(
        reordered.map((t, i) => ({ id: t.id, orderIndex: i })),
      );
    } catch (err) {
      console.error(err);
      alert('Tartibni saqlashda xatolik');
      setTopics(previous);
    } finally {
      setSavingOrder(false);
    }
  };

  if (loading) return <div className="flex justify-center mt-10"><Loader2 className="animate-spin w-8 h-8 text-primary-600" /></div>;

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex items-center gap-4">
          <button onClick={() => navigate(`/courses/${categoryId}`)} className="p-2 rounded-lg hover:bg-gray-100 text-gray-500 transition">
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2 text-sm text-gray-400 mb-0.5">
              <span className="cursor-pointer hover:text-primary-600" onClick={() => navigate('/courses')}>Kurrikulum</span>
              <span>/</span>
              <span className="cursor-pointer hover:text-primary-600" onClick={() => navigate(`/courses/${categoryId}`)}>Modullar</span>
              <span>/</span>
              <span className="text-gray-700 font-medium">{subCategory?.name}</span>
            </div>
            <h1 className="text-2xl font-bold text-gray-900">{subCategory?.name} — Topiclar</h1>
            <p className="text-xs text-gray-400 mt-1">Tartibni o'zgartirish uchun kartalarni sudrab joylashtiring</p>
          </div>
        </div>
        <div className="flex items-center gap-2">
          {savingOrder && <Loader2 className="w-4 h-4 animate-spin text-gray-400" />}
          <button onClick={openCreate} className="flex items-center px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 transition">
            <Plus className="w-5 h-5 mr-2" />
            Yangi Topic
          </button>
        </div>
      </div>

      {isFormOpen && (
        <form onSubmit={handleSave} className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 space-y-5">
          <h2 className="text-lg font-semibold border-b pb-3">
            {editingId ? 'Topicni tahrirlash' : 'Yangi Topic qo\'shish'}
          </h2>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Muqova rasmi</label>
            {(previewUrl || selectedFile) ? (
              <div className="flex items-start gap-4">
                <div className="relative w-32 h-32 rounded-xl overflow-hidden border border-gray-200 flex-shrink-0">
                  <img src={selectedFile ? URL.createObjectURL(selectedFile) : previewUrl!} className="w-full h-full object-cover" />
                  <button type="button" onClick={() => { setSelectedFile(null); setPreviewUrl(null); }}
                    className="absolute top-1 right-1 bg-red-500 text-white p-1 rounded-full hover:bg-red-600 transition">
                    <Trash2 className="w-3 h-3" />
                  </button>
                </div>
                <label className="flex items-center gap-2 px-4 py-2 border border-dashed rounded-xl cursor-pointer hover:border-primary-500 hover:bg-primary-50 transition text-sm text-gray-500">
                  <ImageIcon className="w-4 h-4" />
                  Rasmni almashtirish
                  <input type="file" className="sr-only" onChange={e => setSelectedFile(e.target.files?.[0] ?? null)} accept="image/*" />
                </label>
              </div>
            ) : (
              <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed rounded-xl cursor-pointer hover:border-primary-500 hover:bg-primary-50 transition text-gray-400">
                <ImageIcon className="w-8 h-8 mb-1" />
                <span className="text-sm">Rasm tanlash</span>
                <input type="file" className="sr-only" onChange={e => setSelectedFile(e.target.files?.[0] ?? null)} accept="image/*" />
              </label>
            )}
          </div>

          <div className="flex justify-end gap-3 pt-2 border-t">
            <button type="button" onClick={closeForm} className="px-4 py-2 border rounded-xl text-gray-600 font-medium hover:bg-gray-50 transition">
              Bekor qilish
            </button>
            <button type="submit" disabled={uploading} className="px-4 py-2 bg-primary-600 text-white rounded-xl flex items-center gap-2 hover:bg-primary-700 transition disabled:opacity-60">
              {uploading && <Loader2 className="w-4 h-4 animate-spin" />}
              {uploading ? 'Saqlanmoqda...' : 'Saqlash'}
            </button>
          </div>
        </form>
      )}

      {topics.length === 0 && !isFormOpen ? (
        <div className="bg-white rounded-2xl border border-dashed border-gray-200 p-12 text-center">
          <BookOpen className="w-12 h-12 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-500 font-medium">Hali topic qo'shilmagan</p>
          <p className="text-sm text-gray-400 mt-1">Yuqoridagi "Yangi Topic" tugmasini bosing</p>
        </div>
      ) : (
        <DndContext sensors={sensors} collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
          <SortableContext items={topics.map(t => t.id)} strategy={rectSortingStrategy}>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-5">
              {topics.map((topic, topicIndex) => (
                <SortableTopicCard
                  key={topic.id}
                  topic={topic}
                  topicIndex={topicIndex}
                  onEdit={() => openEdit(topic)}
                  onDelete={() => handleDelete(topic)}
                  onOpen={() => navigate(`/courses/${categoryId}/${subCategoryId}/${topic.id}`)}
                />
              ))}
            </div>
          </SortableContext>
        </DndContext>
      )}
    </div>
  );
};

interface SortableTopicCardProps {
  topic: Topic;
  topicIndex: number;
  onEdit: () => void;
  onDelete: () => void;
  onOpen: () => void;
}

const SortableTopicCard = ({ topic, topicIndex, onEdit, onDelete, onOpen }: SortableTopicCardProps) => {
  const { attributes, listeners, setNodeRef, transform, transition, isDragging } = useSortable({ id: topic.id });
  const style: React.CSSProperties = {
    transform: CSS.Transform.toString(transform),
    transition,
    opacity: isDragging ? 0.4 : 1,
    zIndex: isDragging ? 50 : 'auto',
  };

  return (
    <div ref={setNodeRef} style={style} className="bg-white rounded-2xl border border-gray-100 shadow-sm hover:shadow-md transition-all overflow-hidden">
      <div className="relative h-36 bg-gray-50 flex items-center justify-center overflow-hidden">
        {topic.thumbnailUrl ? (
          <img src={topic.thumbnailUrl} alt={`Topic ${topicIndex + 1}`} className="w-full h-full object-cover" />
        ) : (
          <BookOpen className="w-12 h-12 text-gray-200" />
        )}
        <button
          type="button"
          {...attributes}
          {...listeners}
          className="absolute top-2 left-2 p-1.5 bg-white/90 hover:bg-white text-gray-600 rounded-lg shadow-sm cursor-grab active:cursor-grabbing"
          aria-label="Sudrab joylashtirish"
        >
          <GripVertical className="w-4 h-4" />
        </button>
        <span className="absolute top-2 right-2 px-2 py-0.5 text-xs font-semibold bg-white/90 text-gray-700 rounded-md shadow-sm">
          #{topicIndex + 1}
        </span>
      </div>
      <div className="p-4">
        <div className="flex items-center justify-between mb-3">
          <div>
            <h3 className="font-semibold text-gray-900">Topic #{topicIndex + 1}</h3>
            <p className="text-xs text-gray-400 mt-0.5">{topic.activities?.length ?? 0} ta faoliyat</p>
          </div>
          <div className="flex gap-1">
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); onEdit(); }}
              className="p-2 text-gray-500 hover:text-blue-600 bg-gray-50 hover:bg-blue-50 rounded-lg transition"
              aria-label="Tahrirlash"
            >
              <Edit2 className="w-4 h-4" />
            </button>
            <button
              type="button"
              onClick={(e) => { e.stopPropagation(); onDelete(); }}
              className="p-2 text-gray-500 hover:text-red-600 bg-gray-50 hover:bg-red-50 rounded-lg transition"
              aria-label="O'chirish"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          </div>
        </div>
        <button
          type="button"
          onClick={onOpen}
          className="text-sm font-medium w-full py-2 bg-gray-50 text-gray-700 rounded-lg hover:bg-primary-50 hover:text-primary-700 transition flex items-center justify-center gap-1"
        >
          Contentni boshqarish
          <ChevronRight className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
};
