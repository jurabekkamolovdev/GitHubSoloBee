import { useState, useEffect } from 'react';
import { avatarsApi, type Avatar, type AvatarGender } from '../api/avatars';
import { Loader2, Plus, Trash2, Image as ImageIcon } from 'lucide-react';

export const AvatarManagement = () => {
  const [boyAvatars, setBoyAvatars] = useState<Avatar[]>([]);
  const [girlAvatars, setGirlAvatars] = useState<Avatar[]>([]);
  const [loading, setLoading] = useState(true);

  const fetchAvatars = async () => {
    try {
      const grouped = await avatarsApi.getAvatars();
      setBoyAvatars(grouped.boy ?? []);
      setGirlAvatars(grouped.girl ?? []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchAvatars(); }, []);

  if (loading) {
    return (
      <div className="flex justify-center mt-10">
        <Loader2 className="animate-spin w-8 h-8 text-primary-600" />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <h1 className="text-2xl font-bold text-gray-900">Avatarlar</h1>
        <p className="text-sm text-gray-500 mt-1">
          Profil yaratishda tanlanadigan avatar rasmlari. BOY va GIRL guruhlari alohida boshqariladi.
        </p>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <AvatarGroup title="BOY" gender="BOY" avatars={boyAvatars} onChange={fetchAvatars} />
        <AvatarGroup title="GIRL" gender="GIRL" avatars={girlAvatars} onChange={fetchAvatars} />
      </div>
    </div>
  );
};

function AvatarGroup({
  title,
  gender,
  avatars,
  onChange,
}: {
  title: string;
  gender: AvatarGender;
  avatars: Avatar[];
  onChange: () => void;
}) {
  const [uploading, setUploading] = useState(false);

  const handleUpload = async (file: File) => {
    setUploading(true);
    try {
      const { fileKey } = await avatarsApi.uploadFileToS3(file, 'avatars');
      await avatarsApi.createAvatar({
        gender,
        thumbnailKey: fileKey,
        orderIndex: avatars.length,
      });
      await onChange();
    } catch (err) {
      console.error(err);
      alert("Avatar qo'shishda xatolik");
    } finally {
      setUploading(false);
    }
  };

  const handleDelete = async (id: string) => {
    if (!window.confirm("Bu avatarni o'chirmoqchimisiz?")) return;
    try {
      await avatarsApi.deleteAvatar(id);
      await onChange();
    } catch (err) {
      console.error(err);
      alert("O'chirishda xatolik");
    }
  };

  return (
    <div className="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-bold text-gray-900">{title}</h2>
        <span className="text-xs text-gray-400">{avatars.length} ta avatar</span>
      </div>

      <div className="grid grid-cols-3 sm:grid-cols-4 gap-3">
        {avatars.map(avatar => (
          <div
            key={avatar.id}
            className="relative aspect-square rounded-xl overflow-hidden border border-gray-100 bg-gray-50 group"
          >
            {avatar.thumbnailUrl ? (
              <img src={avatar.thumbnailUrl} className="w-full h-full object-cover" />
            ) : (
              <div className="w-full h-full flex items-center justify-center">
                <ImageIcon className="w-6 h-6 text-gray-300" />
              </div>
            )}
            <button
              onClick={() => handleDelete(avatar.id)}
              className="absolute inset-0 bg-black/50 opacity-0 group-hover:opacity-100 transition flex items-center justify-center"
              title="O'chirish"
            >
              <Trash2 className="w-5 h-5 text-white" />
            </button>
          </div>
        ))}

        <label className="aspect-square rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:border-primary-400 hover:text-primary-600 cursor-pointer transition">
          {uploading ? (
            <Loader2 className="w-5 h-5 animate-spin text-primary-600" />
          ) : (
            <>
              <Plus className="w-5 h-5" />
              <span className="text-xs mt-1">Qo'shish</span>
            </>
          )}
          <input
            type="file"
            className="sr-only"
            accept="image/*"
            disabled={uploading}
            onChange={e => e.target.files?.[0] && handleUpload(e.target.files[0])}
          />
        </label>
      </div>
    </div>
  );
}