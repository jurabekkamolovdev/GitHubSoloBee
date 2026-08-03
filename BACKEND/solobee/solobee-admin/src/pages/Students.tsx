import { useState, useEffect } from 'react';
import { Plus, Loader2, X, ChevronLeft, ChevronRight, GraduationCap, Trash2 } from 'lucide-react';
import { studentsApi, type StudentListItem } from '../api/students';
import { avatarsApi, type Avatar, type AvatarGender } from '../api/avatars';

const LIMIT = 10;

export const Students = () => {
  const [items, setItems] = useState<StudentListItem[]>([]);
  const [total, setTotal] = useState(0);
  const [offset, setOffset] = useState(0);
  const [loading, setLoading] = useState(true);
  const [modalOpen, setModalOpen] = useState(false);
  const [deletingId, setDeletingId] = useState<string | null>(null);

  const fetchStudents = async (newOffset: number) => {
    setLoading(true);
    try {
      const res = await studentsApi.getStudents(newOffset, LIMIT);
      setItems(res.items);
      setTotal(res.total);
      setOffset(newOffset);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchStudents(0); }, []);

  const handleDelete = async (id: string) => {
    if (!confirm("Studentni o'chirishni tasdiqlaysizmi?")) return;

    setDeletingId(id);
    try {
      await studentsApi.deleteStudent(id);
      const nextOffset = items.length === 1 && offset > 0 ? offset - LIMIT : offset;
      await fetchStudents(nextOffset);
    } catch (err) {
      console.error(err);
      alert("O'chirishda xatolik");
    } finally {
      setDeletingId(null);
    }
  };

  const canPrev = offset > 0;
  const canNext = offset + LIMIT < total;
  const rangeStart = total === 0 ? 0 : offset + 1;
  const rangeEnd = Math.min(offset + LIMIT, total);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Studentlar</h1>
          <p className="text-sm text-gray-500 mt-1">{total} ta student ro'yxatdan o'tgan</p>
        </div>
        <button
          onClick={() => setModalOpen(true)}
          className="flex items-center gap-2 px-4 py-2 bg-primary-600 text-white rounded-xl hover:bg-primary-700 transition"
        >
          <Plus className="w-4 h-4" /> Student qo'shish
        </button>
      </div>

      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        {loading ? (
          <div className="flex justify-center py-16">
            <Loader2 className="animate-spin w-8 h-8 text-primary-600" />
          </div>
        ) : items.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-16 text-gray-400">
            <GraduationCap className="w-10 h-10 mb-2" />
            <p className="text-sm">Hali studentlar yo'q</p>
          </div>
        ) : (
          <table className="w-full text-sm">
            <thead>
              <tr className="border-b bg-gray-50 text-left text-gray-500">
                <th className="px-6 py-3 font-medium">Ism</th>
                <th className="px-6 py-3 font-medium">Familiya</th>
                <th className="px-6 py-3 font-medium">Username</th>
                <th className="px-6 py-3 font-medium">Yosh</th>
                <th className="px-6 py-3 font-medium text-right">Amallar</th>
              </tr>
            </thead>
            <tbody>
              {items.map((s) => (
                <tr key={s.id} className="border-b last:border-0 hover:bg-gray-50 transition">
                  <td className="px-6 py-3 text-gray-900">{s.firstName}</td>
                  <td className="px-6 py-3 text-gray-900">{s.lastName}</td>
                  <td className="px-6 py-3 text-gray-500">{s.username}</td>
                  <td className="px-6 py-3 text-gray-500">{s.age}</td>
                  <td className="px-6 py-3 text-right">
                    <button
                      onClick={() => handleDelete(s.id)}
                      disabled={deletingId === s.id}
                      className="p-1.5 rounded-lg text-red-500 hover:bg-red-50 transition disabled:opacity-40"
                      aria-label="O'chirish"
                    >
                      {deletingId === s.id ? (
                        <Loader2 className="w-4 h-4 animate-spin" />
                      ) : (
                        <Trash2 className="w-4 h-4" />
                      )}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}

        {!loading && total > 0 && (
          <div className="flex items-center justify-between px-6 py-4 border-t bg-gray-50">
            <span className="text-sm text-gray-500">
              {rangeStart}–{rangeEnd} / {total}
            </span>
            <div className="flex gap-2">
              <button
                onClick={() => canPrev && fetchStudents(offset - LIMIT)}
                disabled={!canPrev}
                className="flex items-center gap-1 px-3 py-1.5 rounded-lg text-sm border border-gray-200 text-gray-600 hover:bg-gray-100 transition disabled:opacity-40 disabled:cursor-not-allowed"
              >
                <ChevronLeft className="w-4 h-4" /> Oldingi
              </button>
              <button
                onClick={() => canNext && fetchStudents(offset + LIMIT)}
                disabled={!canNext}
                className="flex items-center gap-1 px-3 py-1.5 rounded-lg text-sm border border-gray-200 text-gray-600 hover:bg-gray-100 transition disabled:opacity-40 disabled:cursor-not-allowed"
              >
                Keyingi <ChevronRight className="w-4 h-4" />
              </button>
            </div>
          </div>
        )}
      </div>

      {modalOpen && (
        <CreateStudentModal
          onClose={() => setModalOpen(false)}
          onCreated={() => {
            setModalOpen(false);
            fetchStudents(0);
          }}
        />
      )}
    </div>
  );
};

function CreateStudentModal({
  onClose,
  onCreated,
}: {
  onClose: () => void;
  onCreated: () => void;
}) {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [age, setAge] = useState('');
  const [gender, setGender] = useState<AvatarGender>('BOY');
  const [avatars, setAvatars] = useState<{ boy: Avatar[]; girl: Avatar[] }>({ boy: [], girl: [] });
  const [avatarId, setAvatarId] = useState<string | null>(null);
  const [loadingAvatars, setLoadingAvatars] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    avatarsApi
      .getAvatars()
      .then((res: any) => setAvatars(res.data ?? res))
      .catch((err) => console.error(err))
      .finally(() => setLoadingAvatars(false));
  }, []);

  const visibleAvatars = gender === 'BOY' ? avatars.boy : avatars.girl;

  const handleSubmit = async () => {
    if (!firstName.trim() || !lastName.trim() || !userName.trim() || !password.trim()) {
      return alert("Barcha maydonlarni to'ldiring");
    }
    const ageNum = Number(age);
    if (!age || Number.isNaN(ageNum)) {
      return alert("Yoshni to'g'ri kiriting");
    }
    if (!avatarId) {
      return alert('Avatar tanlang');
    }
    setSaving(true);
    try {
      await studentsApi.createStudent({
        firstName,
        lastName,
        userName,
        password,
        age: ageNum,
        avatarId,
      });
      onCreated();
    } catch (err) {
      console.error(err);
      alert('Student yaratishda xatolik');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-2xl shadow-xl w-full max-w-lg max-h-[90vh] overflow-y-auto">
        <div className="flex items-center justify-between px-6 py-4 border-b">
          <h2 className="text-lg font-bold text-gray-900">Yangi student</h2>
          <button onClick={onClose} className="p-1.5 rounded-lg hover:bg-gray-100 text-gray-400 transition">
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="p-6 space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Ism</label>
              <input
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Familiya</label>
              <input
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
              <input
                value={userName}
                onChange={(e) => setUserName(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Yosh</label>
              <input
                type="number"
                value={age}
                onChange={(e) => setAge(e.target.value)}
                className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Parol</label>
            <input
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Avatar</label>
            <div className="flex gap-2 mb-3">
              {(['BOY', 'GIRL'] as const).map((g) => (
                <button
                  key={g}
                  onClick={() => {
                    setGender(g);
                    setAvatarId(null);
                  }}
                  className={`px-3 py-1.5 rounded-lg text-sm font-medium transition ${
                    gender === g ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                  }`}
                >
                  {g === 'BOY' ? "O'g'il bola" : 'Qiz bola'}
                </button>
              ))}
            </div>

            {loadingAvatars ? (
              <div className="flex justify-center py-8">
                <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
              </div>
            ) : visibleAvatars.length === 0 ? (
              <p className="text-sm text-gray-400 py-4">Bu guruhda avatar topilmadi</p>
            ) : (
              <div className="grid grid-cols-4 gap-3">
                {visibleAvatars.map((a) => (
                  <button
                    key={a.id}
                    onClick={() => setAvatarId(a.id)}
                    className={`aspect-square rounded-xl overflow-hidden border-2 transition ${
                      avatarId === a.id
                        ? 'border-primary-600 ring-2 ring-primary-200'
                        : 'border-gray-100 hover:border-gray-300'
                    }`}
                  >
                    {a.thumbnailUrl ? (
                      <img src={a.thumbnailUrl} className="w-full h-full object-cover" />
                    ) : (
                      <div className="w-full h-full bg-gray-50" />
                    )}
                  </button>
                ))}
              </div>
            )}
          </div>
        </div>

        <div className="flex items-center justify-end gap-3 px-6 py-4 border-t bg-gray-50 rounded-b-2xl">
          <button onClick={onClose} className="px-4 py-2 text-sm text-gray-600 hover:text-gray-900 transition">
            Bekor qilish
          </button>
          <button
            onClick={handleSubmit}
            disabled={saving}
            className="flex items-center gap-2 px-4 py-2 bg-primary-600 text-white rounded-xl text-sm hover:bg-primary-700 transition disabled:opacity-60"
          >
            {saving && <Loader2 className="w-4 h-4 animate-spin" />}
            {saving ? 'Saqlanmoqda...' : 'Yaratish'}
          </button>
        </div>
      </div>
    </div>
  );
}