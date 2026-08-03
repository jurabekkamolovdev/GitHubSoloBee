import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { coursesApi, type Topic, type Activity } from '../api/courses';
import { ArrowLeft, Loader2, Plus, Trash2, Image as ImageIcon, Volume2, Check, Upload } from 'lucide-react';
import { type ActivityType } from '../api/courses';

type Tab = ActivityType;

const TAB_LABELS: Record<Tab, string> = {
  LEARN: 'Learn',
  WRITING: 'Writing',
  WORDHUNT: 'Wordhunt',
  PICQUEST: 'PicQuest',
};

const TABS: Tab[] = ['LEARN', 'WRITING', 'WORDHUNT', 'PICQUEST'];

export const TopicDetail = () => {
  const { categoryId, subCategoryId, topicId } = useParams<{ categoryId: string; subCategoryId: string; topicId: string }>();
  const navigate = useNavigate();

  const [topic, setTopic] = useState<Topic | null>(null);
  const [activities, setActivities] = useState<Activity[]>([]);
  const [activeTab, setActiveTab] = useState<Tab>('LEARN');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const fetchTopic = async () => {
    try {
      const [topicData, activityList] = await Promise.all([
        coursesApi.getTopicDetails(topicId!),
        coursesApi.getActivitiesByTopic(topicId!),
      ]);
      setTopic(topicData);
      setActivities(activityList ?? []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchTopic(); }, [topicId]);

  const getActivity = (type: Tab): Activity | undefined =>
    activities.find(a => a.type === type);

  const saveActivity = async (type: Tab, payload: Record<string, any>) => {
    const existing = getActivity(type);
    try {
      setSaving(true);
      if (existing) {
        await coursesApi.updateActivity(existing.id, { payload });
      } else {
        await coursesApi.createActivity({ type, topicId: topicId!, payload, orderIndex: TABS.indexOf(type) });
      }
      await fetchTopic();
    } catch (err) {
      console.error(err);
      alert('Saqlashda xatolik');
    } finally {
      setSaving(false);
    }
  };

  const deleteActivity = async (type: Tab) => {
    const existing = getActivity(type);
    if (!existing || !window.confirm(`"${TAB_LABELS[type]}" faoliyatini o'chirmoqchimisiz?`)) return;
    try {
      await coursesApi.deleteActivity(existing.id);
      await fetchTopic();
    } catch (err) {
      console.error(err);
    }
  };

  if (loading) return <div className="flex justify-center mt-10"><Loader2 className="animate-spin w-8 h-8 text-primary-600" /></div>;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
        <div className="flex items-center gap-4">
          <button onClick={() => navigate(`/courses/${categoryId}/${subCategoryId}`)} className="p-2 rounded-lg hover:bg-gray-100 text-gray-500 transition">
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div>
            <div className="flex items-center gap-2 text-sm text-gray-400 mb-0.5">
              <span className="cursor-pointer hover:text-primary-600" onClick={() => navigate('/courses')}>Kurrikulum</span>
              <span>/</span>
              <span className="cursor-pointer hover:text-primary-600" onClick={() => navigate(`/courses/${categoryId}`)}>Modullar</span>
              <span>/</span>
              <span className="cursor-pointer hover:text-primary-600" onClick={() => navigate(`/courses/${categoryId}/${subCategoryId}`)}>Topiclar</span>
              <span>/</span>
              <span className="text-gray-700 font-medium">Topic</span>
            </div>
            <h1 className="text-2xl font-bold text-gray-900">Topic — Content</h1>
          </div>
        </div>
      </div>

      {/* Topic info card */}
      <div className="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 flex items-center gap-6">
        <div className="w-24 h-24 rounded-xl overflow-hidden border border-gray-100 flex-shrink-0 bg-gray-50 flex items-center justify-center">
          {topic?.thumbnailUrl
            ? <img src={topic.thumbnailUrl} alt="Topic" className="w-full h-full object-cover" />
            : <ImageIcon className="w-8 h-8 text-gray-300" />
          }
        </div>
        <div>
          <h2 className="text-xl font-bold text-gray-900">Topic</h2>
          <p className="text-sm text-gray-500 mt-1">{activities.length} ta faoliyat sozlangan</p>
        </div>
      </div>

      {/* Activity tabs */}
      <div className="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden">
        <div className="flex border-b">
          {TABS.map(tab => {
            const activity = getActivity(tab);
            return (
              <button
                key={tab}
                onClick={() => setActiveTab(tab)}
                className={`flex-1 py-4 text-sm font-medium transition relative ${
                  activeTab === tab ? 'text-primary-600 border-b-2 border-primary-600' : 'text-gray-500 hover:text-gray-700'
                }`}
              >
                {TAB_LABELS[tab]}
                {activity && (
                  <span className="absolute top-2 right-2 w-2 h-2 rounded-full bg-green-500" />
                )}
              </button>
            );
          })}
        </div>

        <div className="p-6">
          {activeTab === 'LEARN' && (
            <LearnEditor activity={getActivity('LEARN')} onSave={p => saveActivity('LEARN', p)} onDelete={() => deleteActivity('LEARN')} saving={saving} />
          )}
          {activeTab === 'WRITING' && (
            <WritingEditor activity={getActivity('WRITING')} onSave={p => saveActivity('WRITING', p)} onDelete={() => deleteActivity('WRITING')} saving={saving} />
          )}
          {activeTab === 'WORDHUNT' && (
            <WordhuntEditor activity={getActivity('WORDHUNT')} onSave={p => saveActivity('WORDHUNT', p)} onDelete={() => deleteActivity('WORDHUNT')} saving={saving} />
          )}
          {activeTab === 'PICQUEST' && (
            <PicQuestEditor activity={getActivity('PICQUEST')} onSave={p => saveActivity('PICQUEST', p)} onDelete={() => deleteActivity('PICQUEST')} saving={saving} />
          )}
        </div>
      </div>
    </div>
  );
};

// ── Activity editors ─────────────────────────────────────────

interface EditorProps {
  activity?: Activity;
  onSave: (payload: Record<string, any>) => void;
  onDelete: () => void;
  saving: boolean;
}

function LearnEditor({ activity, onSave, onDelete, saving }: EditorProps) {
  const initialImageUrl: string | null = activity?.payload?.imageUrl ?? null;
  const initialAudioUrl: string | null = activity?.payload?.audioUrl ?? null;

  const [imageUrl, setImageUrl] = useState<string | null>(initialImageUrl);
  const [audioUrl, setAudioUrl] = useState<string | null>(initialAudioUrl);
  // Only set when the user uploads a new file in this session. On save we submit
  // these so the backend knows which keys changed; omitted keys stay untouched.
  const [newImageKey, setNewImageKey] = useState<string | null | undefined>();
  const [newAudioKey, setNewAudioKey] = useState<string | null | undefined>();
  const [uploadingImage, setUploadingImage] = useState(false);
  const [uploadingAudio, setUploadingAudio] = useState(false);

  const handleImageUpload = async (file: File) => {
    setUploadingImage(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'learn');
      setNewImageKey(fileKey);
      setImageUrl(publicUrl);
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      setUploadingImage(false);
    }
  };

  const handleAudioUpload = async (file: File) => {
    setUploadingAudio(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'learn');
      setNewAudioKey(fileKey);
      setAudioUrl(publicUrl);
    } catch {
      alert('Audio yuklashda xatolik');
    } finally {
      setUploadingAudio(false);
    }
  };

  const handleSave = () => {
    const payload: Record<string, any> = {};
    if (newImageKey !== undefined) payload.imageKey = newImageKey;
    if (newAudioKey !== undefined) payload.audioKey = newAudioKey;
    // For create: at least one media must be provided explicitly.
    if (!activity && !payload.imageKey && !payload.audioKey) {
      return alert('Kamida bitta fayl yuklang');
    }
    onSave(payload);
  };

  return (
    <div className="space-y-6 max-w-lg">
      <p className="text-sm text-gray-500">Learn ekranida ko'rinadigan katta rasm va talaffuz audiosi.</p>

      {/* Image picker */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-2">Hero rasm</label>
        <div className="relative w-full aspect-video rounded-xl overflow-hidden bg-gray-50 border flex items-center justify-center">
          {uploadingImage ? (
            <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
          ) : imageUrl ? (
            <>
              <img src={imageUrl} className="w-full h-full object-cover" />
              <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                <ImageIcon className="w-6 h-6 text-white" />
                <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleImageUpload(e.target.files[0])} />
              </label>
            </>
          ) : (
            <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
              <ImageIcon className="w-6 h-6 text-gray-400" />
              <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
              <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleImageUpload(e.target.files[0])} />
            </label>
          )}
        </div>
      </div>

      {/* Audio picker */}
      <div>
        <label className="block text-sm font-medium text-gray-700 mb-2">Audio</label>
        <AudioPicker
          audioUrl={audioUrl ?? ''}
          uploading={uploadingAudio}
          onPick={handleAudioUpload}
          onClear={() => { setNewAudioKey(null); setAudioUrl(null); }}
        />
      </div>

      <EditorFooter activity={activity} onSave={handleSave} onDelete={onDelete} saving={saving} />
    </div>
  );
}

// interface SpellOption {
//   char: string;
//   imageUrl: string | null;
//   newImageKey?: string;
//   uploading?: boolean;
// }

// function WritingEditor({ activity, onSave, onDelete, saving }: EditorProps) {
//   const [mode, setMode] = useState<'trace' | 'spell'>(activity?.payload?.mode ?? 'trace');
//   const [answer, setAnswer] = useState(activity?.payload?.answer ?? '');
//   const [traceChar, setTraceChar] = useState(activity?.payload?.char ?? '');

//   // ── root-level image ──────────────────────────────────────
//   const [imageUrl, setImageUrl] = useState<string | null>(activity?.payload?.imageUrl ?? null);
//   const [newImageKey, setNewImageKey] = useState<string | null | undefined>(undefined);
//   const [uploadingImage, setUploadingImage] = useState(false);

//   // ── root-level audio ──────────────────────────────────────
//   const [audioUrl, setAudioUrl] = useState<string | null>(activity?.payload?.audioUrl ?? null);
//   const [newAudioKey, setNewAudioKey] = useState<string | null | undefined>(undefined);
//   const [uploadingAudio, setUploadingAudio] = useState(false);

//   // ── options ───────────────────────────────────────────────
//   const [spellOptions, setSpellOptions] = useState<SpellOption[]>(
//     activity?.payload?.options?.map((o: any) => ({
//       char: o.char ?? '',
//       imageUrl: o.imageUrl ?? null,
//     })) ?? []
//   );

//   const addOption = () =>
//     setSpellOptions(prev => [...prev, { char: '', imageUrl: null }]);

//   const removeOption = (idx: number) =>
//     setSpellOptions(prev => prev.filter((_, i) => i !== idx));

//   const updateOption = (idx: number, patch: Partial<SpellOption>) =>
//     setSpellOptions(prev => prev.map((o, i) => i === idx ? { ...o, ...patch } : o));

//   const handleRootImageUpload = async (file: File) => {
//     setUploadingImage(true);
//     try {
//       const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
//       setNewImageKey(fileKey);
//       setImageUrl(publicUrl);
//     } catch {
//       alert('Rasm yuklashda xatolik');
//     } finally {
//       setUploadingImage(false);
//     }
//   };

//   const handleOptionImageUpload = async (idx: number, file: File) => {
//     updateOption(idx, { uploading: true });
//     try {
//       const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
//       updateOption(idx, { newImageKey: fileKey, imageUrl: publicUrl });
//     } catch {
//       alert('Rasm yuklashda xatolik');
//     } finally {
//       updateOption(idx, { uploading: false });
//     }
//   };

//   const handleAudioUpload = async (file: File) => {
//     setUploadingAudio(true);
//     try {
//       const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
//       setNewAudioKey(fileKey);
//       setAudioUrl(publicUrl);
//     } catch {
//       alert('Audio yuklashda xatolik');
//     } finally {
//       setUploadingAudio(false);
//     }
//   };

//   const handleSave = () => {
//     if (mode === 'trace') { onSave({ mode: 'trace' }); return; }
//     if (!answer.trim()) return alert('Answer kiritilishi shart');
//     if (spellOptions.length === 0) return alert("Kamida 1 ta option qo'shilishi shart");
//     if (spellOptions.some(o => !o.char.trim())) return alert('Barcha harflar kiritilishi shart');
//     if (spellOptions.some(o => !o.imageUrl)) return alert('Barcha rasmlar yuklanishi shart');

//     onSave({
//       mode: 'spell',
//       answer: answer.toUpperCase(),
//       ...(newImageKey !== undefined ? { imageKey: newImageKey } : {}),
//       ...(newAudioKey !== undefined ? { audioKey: newAudioKey } : {}),
//       options: spellOptions.map(o => ({
//         char: o.char.toUpperCase(),
//         ...(o.newImageKey ? { imageKey: o.newImageKey } : {}),
//       })),
//     });
//   };

//   return (
//     <div className="space-y-4 max-w-2xl">
//       {/* Mode toggle */}
//       <div className="flex gap-3">
//         {(['trace', 'spell'] as const).map(m => (
//           <button
//             key={m}
//             onClick={() => setMode(m)}
//             className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
//               mode === m ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
//             }`}
//           >
//             {m === 'trace' ? 'Trace (harf chizish)' : "Spell (so'z yig'ish)"}
//           </button>
//         ))}
//       </div>

//       {mode === 'spell' && (
//         <div className="space-y-4">

//           {/* ── Umumiy rasm ── */}
//           <div>
//             <label className="block text-sm font-medium text-gray-700 mb-2">Umumiy rasm</label>
//             <div className="relative w-full aspect-video rounded-xl overflow-hidden bg-gray-50 border flex items-center justify-center max-w-sm">
//               {uploadingImage ? (
//                 <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
//               ) : imageUrl ? (
//                 <>
//                   <img src={imageUrl} className="w-full h-full object-cover" />
//                   <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
//                     <ImageIcon className="w-6 h-6 text-white" />
//                     <input type="file" className="sr-only" accept="image/*"
//                       onChange={e => e.target.files?.[0] && handleRootImageUpload(e.target.files[0])} />
//                   </label>
//                 </>
//               ) : (
//                 <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
//                   <ImageIcon className="w-6 h-6 text-gray-400" />
//                   <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
//                   <input type="file" className="sr-only" accept="image/*"
//                     onChange={e => e.target.files?.[0] && handleRootImageUpload(e.target.files[0])} />
//                 </label>
//               )}
//             </div>
//           </div>

//           {/* ── To'g'ri javob ── */}
//           <div>
//             <label className="block text-sm font-medium text-gray-700">To'g'ri javob (so'z)</label>
//             <input
//               type="text"
//               value={answer}
//               onChange={e => setAnswer(e.target.value.toUpperCase())}
//               className="mt-1 block w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase tracking-widest font-mono"
//               placeholder="BANANA"
//             />
//           </div>

//           {/* ── Audio ── */}
//           <div>
//             <label className="block text-sm font-medium text-gray-700 mb-2">Audio</label>
//             <AudioPicker
//               audioUrl={audioUrl ?? ''}
//               uploading={uploadingAudio}
//               onPick={handleAudioUpload}
//               onClear={() => { setNewAudioKey(null); setAudioUrl(null); }}
//             />
//           </div>

//           {/* ── Options ── */}
//           <div>
//             <div className="flex items-center justify-between mb-2">
//               <label className="block text-sm font-medium text-gray-700">
//                 Variantlar
//                 <span className="ml-2 text-xs text-gray-400 font-normal">(bola uchun aralashtiriladi)</span>
//               </label>
//               <span className="text-xs text-gray-400">{spellOptions.length} ta</span>
//             </div>

//             <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3">
//               {spellOptions.map((opt, idx) => (
//                 <div key={idx} className="rounded-xl border border-gray-200 p-2 space-y-2">
//                   <div className="relative aspect-square rounded-lg overflow-hidden bg-gray-50 flex items-center justify-center border">
//                     {opt.uploading ? (
//                       <Loader2 className="w-5 h-5 animate-spin text-primary-600" />
//                     ) : opt.imageUrl ? (
//                       <>
//                         <img src={opt.imageUrl} className="w-full h-full object-cover" alt={opt.char} />
//                         <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
//                           <ImageIcon className="w-5 h-5 text-white" />
//                           <input type="file" className="sr-only" accept="image/*"
//                             onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
//                         </label>
//                       </>
//                     ) : (
//                       <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
//                         <ImageIcon className="w-5 h-5 text-gray-400" />
//                         <span className="text-xs text-gray-400 mt-1">Rasm</span>
//                         <input type="file" className="sr-only" accept="image/*"
//                           onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
//                       </label>
//                     )}
//                   </div>
//                   <input
//                     type="text"
//                     value={opt.char}
//                     maxLength={1}
//                     onChange={e => updateOption(idx, { char: e.target.value.toUpperCase() })}
//                     placeholder="A"
//                     className="w-full p-1.5 text-center text-lg font-bold font-mono border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase"
//                   />
//                   <button
//                     onClick={() => removeOption(idx)}
//                     className="w-full text-xs text-red-400 hover:text-red-600 transition py-0.5"
//                   >
//                     O'chirish
//                   </button>
//                 </div>
//               ))}

//               <button
//                 onClick={addOption}
//                 className="aspect-square rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:border-primary-400 hover:text-primary-600 transition"
//               >
//                 <Plus className="w-6 h-6" />
//                 <span className="text-xs mt-1">Qo'shish</span>
//               </button>
//             </div>
//           </div>
//         </div>
//       )}

//       <EditorFooter activity={activity} onSave={handleSave} onDelete={onDelete} saving={saving} />
//     </div>
//   );
// }

interface SpellOption {
  char: string;
  imageUrl: string | null;
  newImageKey?: string;
  uploading?: boolean;
}

function WritingEditor({ activity, onSave, onDelete, saving }: EditorProps) {
  const [mode, setMode] = useState<'trace' | 'spell'>(activity?.payload?.mode ?? 'trace');
  const [answer, setAnswer] = useState(activity?.payload?.answer ?? '');

  // ── trace-level char ───────────────────────────────────────
  const [traceChar, setTraceChar] = useState(activity?.payload?.char ?? '');

  // ── root-level image ──────────────────────────────────────
  const [imageUrl, setImageUrl] = useState<string | null>(activity?.payload?.imageUrl ?? null);
  const [newImageKey, setNewImageKey] = useState<string | null | undefined>(undefined);
  const [uploadingImage, setUploadingImage] = useState(false);

  // ── root-level audio ──────────────────────────────────────
  const [audioUrl, setAudioUrl] = useState<string | null>(activity?.payload?.audioUrl ?? null);
  const [newAudioKey, setNewAudioKey] = useState<string | null | undefined>(undefined);
  const [uploadingAudio, setUploadingAudio] = useState(false);

  // ── options ───────────────────────────────────────────────
  const [spellOptions, setSpellOptions] = useState<SpellOption[]>(
    activity?.payload?.options?.map((o: any) => ({
      char: o.char ?? '',
      imageUrl: o.imageUrl ?? null,
    })) ?? []
  );

  const addOption = () =>
    setSpellOptions(prev => [...prev, { char: '', imageUrl: null }]);

  const removeOption = (idx: number) =>
    setSpellOptions(prev => prev.filter((_, i) => i !== idx));

  const updateOption = (idx: number, patch: Partial<SpellOption>) =>
    setSpellOptions(prev => prev.map((o, i) => i === idx ? { ...o, ...patch } : o));

  const handleRootImageUpload = async (file: File) => {
    setUploadingImage(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
      setNewImageKey(fileKey);
      setImageUrl(publicUrl);
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      setUploadingImage(false);
    }
  };

  const handleOptionImageUpload = async (idx: number, file: File) => {
    updateOption(idx, { uploading: true });
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
      updateOption(idx, { newImageKey: fileKey, imageUrl: publicUrl });
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      updateOption(idx, { uploading: false });
    }
  };

  const handleAudioUpload = async (file: File) => {
    setUploadingAudio(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'writing');
      setNewAudioKey(fileKey);
      setAudioUrl(publicUrl);
    } catch {
      alert('Audio yuklashda xatolik');
    } finally {
      setUploadingAudio(false);
    }
  };

  const handleSave = () => {
    if (mode === 'trace') {
      if (!traceChar.trim()) return alert('Harf kiritilishi shart');
      onSave({ mode: 'trace', char: traceChar.toUpperCase() });
      return;
    }
    if (!answer.trim()) return alert('Answer kiritilishi shart');
    if (spellOptions.length === 0) return alert("Kamida 1 ta option qo'shilishi shart");
    if (spellOptions.some(o => !o.char.trim())) return alert('Barcha harflar kiritilishi shart');
    if (spellOptions.some(o => !o.imageUrl)) return alert('Barcha rasmlar yuklanishi shart');

    onSave({
      mode: 'spell',
      answer: answer.toUpperCase(),
      ...(newImageKey !== undefined ? { imageKey: newImageKey } : {}),
      ...(newAudioKey !== undefined ? { audioKey: newAudioKey } : {}),
      options: spellOptions.map(o => ({
        char: o.char.toUpperCase(),
        ...(o.newImageKey ? { imageKey: o.newImageKey } : {}),
      })),
    });
  };

  return (
    <div className="space-y-4 max-w-2xl">
      {/* Mode toggle */}
      <div className="flex gap-3">
        {(['trace', 'spell'] as const).map(m => (
          <button
            key={m}
            onClick={() => setMode(m)}
            className={`px-4 py-2 rounded-lg text-sm font-medium transition ${
              mode === m ? 'bg-primary-600 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
            }`}
          >
            {m === 'trace' ? 'Trace (harf chizish)' : "Spell (so'z yig'ish)"}
          </button>
        ))}
      </div>

      {mode === 'trace' && (
        <div className="max-w-xs">
          <label className="block text-sm font-medium text-gray-700">Chiziladigan harf</label>
          <input
            type="text"
            value={traceChar}
            maxLength={1}
            onChange={e => setTraceChar(e.target.value.toUpperCase())}
            placeholder="A"
            className="mt-1 w-20 p-2 text-center text-2xl font-bold font-mono border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase tracking-widest"
          />
        </div>
      )}

      {mode === 'spell' && (
        <div className="space-y-4">

          {/* ── Umumiy rasm ── */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Umumiy rasm</label>
            <div className="relative w-full aspect-video rounded-xl overflow-hidden bg-gray-50 border flex items-center justify-center max-w-sm">
              {uploadingImage ? (
                <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
              ) : imageUrl ? (
                <>
                  <img src={imageUrl} className="w-full h-full object-cover" />
                  <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                    <ImageIcon className="w-6 h-6 text-white" />
                    <input type="file" className="sr-only" accept="image/*"
                      onChange={e => e.target.files?.[0] && handleRootImageUpload(e.target.files[0])} />
                  </label>
                </>
              ) : (
                <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
                  <ImageIcon className="w-6 h-6 text-gray-400" />
                  <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
                  <input type="file" className="sr-only" accept="image/*"
                    onChange={e => e.target.files?.[0] && handleRootImageUpload(e.target.files[0])} />
                </label>
              )}
            </div>
          </div>

          {/* ── To'g'ri javob ── */}
          <div>
            <label className="block text-sm font-medium text-gray-700">To'g'ri javob (so'z)</label>
            <input
              type="text"
              value={answer}
              onChange={e => setAnswer(e.target.value.toUpperCase())}
              className="mt-1 block w-full p-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase tracking-widest font-mono"
              placeholder="BANANA"
            />
          </div>

          {/* ── Audio ── */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">Audio</label>
            <AudioPicker
              audioUrl={audioUrl ?? ''}
              uploading={uploadingAudio}
              onPick={handleAudioUpload}
              onClear={() => { setNewAudioKey(null); setAudioUrl(null); }}
            />
          </div>

          {/* ── Options ── */}
          <div>
            <div className="flex items-center justify-between mb-2">
              <label className="block text-sm font-medium text-gray-700">
                Variantlar
                <span className="ml-2 text-xs text-gray-400 font-normal">(bola uchun aralashtiriladi)</span>
              </label>
              <span className="text-xs text-gray-400">{spellOptions.length} ta</span>
            </div>

            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3">
              {spellOptions.map((opt, idx) => (
                <div key={idx} className="rounded-xl border border-gray-200 p-2 space-y-2">
                  <div className="relative aspect-square rounded-lg overflow-hidden bg-gray-50 flex items-center justify-center border">
                    {opt.uploading ? (
                      <Loader2 className="w-5 h-5 animate-spin text-primary-600" />
                    ) : opt.imageUrl ? (
                      <>
                        <img src={opt.imageUrl} className="w-full h-full object-cover" alt={opt.char} />
                        <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                          <ImageIcon className="w-5 h-5 text-white" />
                          <input type="file" className="sr-only" accept="image/*"
                            onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
                        </label>
                      </>
                    ) : (
                      <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
                        <ImageIcon className="w-5 h-5 text-gray-400" />
                        <span className="text-xs text-gray-400 mt-1">Rasm</span>
                        <input type="file" className="sr-only" accept="image/*"
                          onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
                      </label>
                    )}
                  </div>
                  <input
                    type="text"
                    value={opt.char}
                    maxLength={1}
                    onChange={e => updateOption(idx, { char: e.target.value.toUpperCase() })}
                    placeholder="A"
                    className="w-full p-1.5 text-center text-lg font-bold font-mono border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500 uppercase"
                  />
                  <button
                    onClick={() => removeOption(idx)}
                    className="w-full text-xs text-red-400 hover:text-red-600 transition py-0.5"
                  >
                    O'chirish
                  </button>
                </div>
              ))}

              <button
                onClick={addOption}
                className="aspect-square rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:border-primary-400 hover:text-primary-600 transition"
              >
                <Plus className="w-6 h-6" />
                <span className="text-xs mt-1">Qo'shish</span>
              </button>
            </div>
          </div>
        </div>
      )}

      <EditorFooter activity={activity} onSave={handleSave} onDelete={onDelete} saving={saving} />
    </div>
  );
}

type WordhuntOption = {
  imageUrl: string | null;
  newImageKey?: string; // fresh upload this session; server keeps existing key if omitted
  audioUrl: string | null;
  newAudioKey?: string | null; // set only when the user attaches/clears audio locally
  isCorrect: boolean;
  uploadingImage?: boolean;
  uploadingAudio?: boolean;
};


function WordhuntEditor({ activity, onSave, onDelete, saving }: EditorProps) {
  const [options, setOptions] = useState<WordhuntOption[]>(
    activity?.payload?.options?.map((o: any) => ({
      imageUrl: o.imageUrl ?? null,
      audioUrl: o.audioUrl ?? null,
      isCorrect: o.isCorrect,
    })) ??
    [
      { imageUrl: null, audioUrl: null, isCorrect: true },
      { imageUrl: null, audioUrl: null, isCorrect: false },
      { imageUrl: null, audioUrl: null, isCorrect: false },
      { imageUrl: null, audioUrl: null, isCorrect: false },
    ]
  );
  const [imageUrl, setImageUrl] = useState<string | null>(activity?.payload?.imageUrl ?? null);
  const [newImageKey, setNewImageKey] = useState<string | null | undefined>();
  const [uploadingImage, setUploadingImage] = useState(false);

  const checkerboard = {
    backgroundImage: 'repeating-conic-gradient(#e5e7eb 0% 25%, white 0% 50%)',
    backgroundSize: '16px 16px',
  };

  const setCorrect = (idx: number) => setOptions(prev => prev.map((o, i) => ({ ...o, isCorrect: i === idx })));
  const update = (idx: number, patch: Partial<WordhuntOption>) =>
    setOptions(prev => prev.map((o, i) => i === idx ? { ...o, ...patch } : o));

  const handleOptionImageUpload = async (idx: number, file: File) => {
    update(idx, { uploadingImage: true });
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'wordhunt');
      update(idx, { newImageKey: fileKey, imageUrl: publicUrl });
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      update(idx, { uploadingImage: false });
    }
  };

  const handleOptionAudioUpload = async (idx: number, file: File) => {
    update(idx, { uploadingAudio: true });
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'wordhunt');
      update(idx, { newAudioKey: fileKey, audioUrl: publicUrl });
    } catch {
      alert('Audio yuklashda xatolik');
    } finally {
      update(idx, { uploadingAudio: false });
    }
  };

  const handleImageUpload = async (file: File) => {
    setUploadingImage(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'wordhunt');
      setNewImageKey(fileKey);
      setImageUrl(publicUrl);
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      setUploadingImage(false);
    }
  };

  const handleSave = () => {
    if (options.some(o => !o.imageUrl)) return alert('Barcha variant rasmlari yuklanishi shart');
    if (!options.some(o => o.isCorrect)) return alert('Kamida 1 ta to\'g\'ri javob belgilanishi shart');
    onSave({
      ...(newImageKey !== undefined ? { imageKey: newImageKey } : {}),
      options: options.map(o => ({
        isCorrect: o.isCorrect,
        ...(o.newImageKey ? { imageKey: o.newImageKey } : {}),
        ...(o.newAudioKey !== undefined ? { audioKey: o.newAudioKey } : {}),
      })),
    });
  };

  return (
    <div className="space-y-4">
      <p className="text-sm text-gray-500">Tepadagi rasm — o'quvchi topishi kerak bo'lgan harf. Har bir variant to'liq tayyorlangan rasm sifatida yuklanadi (gradient, fon rangi, matn allaqachon chizilgan). Ixtiyoriy audio biriktirish mumkin.</p>

      <div className="max-w-xs">
        <label className="block text-sm font-medium text-gray-700 mb-2">Topiladigan harf (rasm)</label>
        <div
          className="relative aspect-square rounded-xl overflow-hidden border flex items-center justify-center"
          style={checkerboard}
        >
          {uploadingImage ? (
            <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
          ) : imageUrl ? (
            <>
              <img src={imageUrl} className="w-full h-full object-contain" />
              <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                <ImageIcon className="w-6 h-6 text-white" />
                <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleImageUpload(e.target.files[0])} />
              </label>
            </>
          ) : (
            <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-white/50 transition">
              <ImageIcon className="w-6 h-6 text-gray-400" />
              <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
              <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleImageUpload(e.target.files[0])} />
            </label>
          )}
        </div>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {options.map((opt, idx) => (
          <div key={idx} className={`rounded-xl border p-3 space-y-2 ${opt.isCorrect ? 'border-green-300 bg-green-50' : 'border-gray-100'}`}>
            <div
              className="relative aspect-square rounded-lg overflow-hidden flex items-center justify-center border"
              style={checkerboard}
            >
              {opt.uploadingImage ? (
                <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
              ) : opt.imageUrl ? (
                <>
                  <img src={opt.imageUrl} className="w-full h-full object-contain" />
                  <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                    <ImageIcon className="w-6 h-6 text-white" />
                    <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
                  </label>
                </>
              ) : (
                <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-white/50 transition">
                  <ImageIcon className="w-6 h-6 text-gray-400" />
                  <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
                  <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleOptionImageUpload(idx, e.target.files[0])} />
                </label>
              )}
            </div>
            <AudioPicker
              audioUrl={opt.audioUrl ?? ''}
              uploading={!!opt.uploadingAudio}
              compact
              onPick={file => handleOptionAudioUpload(idx, file)}
              onClear={() => update(idx, { newAudioKey: null, audioUrl: null })}
            />
            <button onClick={() => setCorrect(idx)} className={`w-full flex items-center justify-center gap-1.5 py-1.5 rounded-lg text-xs font-medium transition ${opt.isCorrect ? 'bg-green-500 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}>
              <Check className="w-3 h-3" />
              {opt.isCorrect ? 'To\'g\'ri javob' : 'Belgilash'}
            </button>
            {options.length > 2 && (
              <button onClick={() => setOptions(prev => prev.filter((_, i) => i !== idx))} className="w-full text-xs text-red-400 hover:text-red-600 transition">
                O'chirish
              </button>
            )}
          </div>
        ))}
        <button
          onClick={() => setOptions(prev => [...prev, { imageUrl: null, audioUrl: null, isCorrect: false }])}
          className="aspect-square rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:border-primary-400 hover:text-primary-600 transition"
        >
          <Plus className="w-6 h-6" />
          <span className="text-xs mt-1">Qo'shish</span>
        </button>
      </div>
      <EditorFooter activity={activity} onSave={handleSave} onDelete={onDelete} saving={saving} />
    </div>
  );
}

type PicQuestOption = {
  imageUrl: string | null;
  newImageKey?: string; // fresh upload this session; server keeps existing key if omitted
  label: string;
  isCorrect: boolean;
  uploading?: boolean;
};

function PicQuestEditor({ activity, onSave, onDelete, saving }: EditorProps) {
  const [options, setOptions] = useState<PicQuestOption[]>(
    activity?.payload?.options?.map((o: any) => ({
      imageUrl: o.imageUrl ?? null,
      label: o.label ?? '',
      isCorrect: o.isCorrect,
    })) ??
    [
      { imageUrl: null, label: '', isCorrect: true },
      { imageUrl: null, label: '', isCorrect: false },
      { imageUrl: null, label: '', isCorrect: false },
      { imageUrl: null, label: '', isCorrect: false },
    ]
  );
  const [audioUrl, setAudioUrl] = useState<string | null>(activity?.payload?.audioUrl ?? null);
  const [newAudioKey, setNewAudioKey] = useState<string | null | undefined>(undefined);
  const [uploadingAudio, setUploadingAudio] = useState(false);

  const setCorrect = (idx: number) => setOptions(prev => prev.map((o, i) => ({ ...o, isCorrect: i === idx })));
  const update = (idx: number, patch: Partial<PicQuestOption>) =>
    setOptions(prev => prev.map((o, i) => i === idx ? { ...o, ...patch } : o));

  const handleUpload = async (idx: number, file: File) => {
    update(idx, { uploading: true });
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'picquest');
      update(idx, { newImageKey: fileKey, imageUrl: publicUrl });
    } catch {
      alert('Rasm yuklashda xatolik');
    } finally {
      update(idx, { uploading: false });
    }
  };

  const handleAudioUpload = async (file: File) => {
    setUploadingAudio(true);
    try {
      const { fileKey, publicUrl } = await coursesApi.uploadFileToS3(file, 'picquest');
      setNewAudioKey(fileKey);
      setAudioUrl(publicUrl);
    } catch {
      alert('Audio yuklashda xatolik');
    } finally {
      setUploadingAudio(false);
    }
  };

  const handleSave = () => {
    if (options.some(o => !o.imageUrl)) return alert('Barcha rasmlar yuklanishi shart');
    if (!options.some(o => o.isCorrect)) return alert('Kamida 1 ta to\'g\'ri javob belgilanishi shart');
    onSave({
      ...(newAudioKey !== undefined ? { audioKey: newAudioKey } : {}),
      options: options.map(o => ({
        isCorrect: o.isCorrect,
        ...(o.label ? { label: o.label } : {}),
        ...(o.newImageKey ? { imageKey: o.newImageKey } : {}),
      })),
    });
  };

  return (
    <div className="space-y-4">
      <p className="text-sm text-gray-500">
        O'quvchi avval prompt audioni tinglaydi, keyin mos rasmni tanlaydi. To'g'ri rasmni radio tugma bilan belgilang.
      </p>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-2">Prompt audio</label>
        <AudioPicker
          audioUrl={audioUrl ?? ''}
          uploading={uploadingAudio}
          onPick={handleAudioUpload}
          onClear={() => { setNewAudioKey(null); setAudioUrl(null); }}
        />
      </div>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        {options.map((opt, idx) => (
          <div key={idx} className={`rounded-xl border p-3 space-y-2 ${opt.isCorrect ? 'border-green-300 bg-green-50' : 'border-gray-100'}`}>
            <div className="relative aspect-square rounded-lg overflow-hidden bg-gray-50 flex items-center justify-center border">
              {opt.uploading ? (
                <Loader2 className="w-6 h-6 animate-spin text-primary-600" />
              ) : opt.imageUrl ? (
                <>
                  <img src={opt.imageUrl} className="w-full h-full object-cover" />
                  <label className="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition flex items-center justify-center cursor-pointer">
                    <ImageIcon className="w-6 h-6 text-white" />
                    <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleUpload(idx, e.target.files[0])} />
                  </label>
                </>
              ) : (
                <label className="absolute inset-0 flex flex-col items-center justify-center cursor-pointer hover:bg-gray-100 transition">
                  <ImageIcon className="w-6 h-6 text-gray-400" />
                  <span className="text-xs text-gray-400 mt-1">Rasm yuklash</span>
                  <input type="file" className="sr-only" accept="image/*" onChange={e => e.target.files?.[0] && handleUpload(idx, e.target.files[0])} />
                </label>
              )}
            </div>
            <input
              type="text"
              value={opt.label}
              onChange={e => update(idx, { label: e.target.value })}
              placeholder="Label (ixtiyoriy)"
              className="w-full p-1.5 text-xs border rounded-lg focus:outline-none focus:ring-2 focus:ring-primary-500"
            />
            <button onClick={() => setCorrect(idx)} className={`w-full flex items-center justify-center gap-1.5 py-1.5 rounded-lg text-xs font-medium transition ${opt.isCorrect ? 'bg-green-500 text-white' : 'bg-gray-100 text-gray-600 hover:bg-gray-200'}`}>
              <Check className="w-3 h-3" />
              {opt.isCorrect ? 'To\'g\'ri javob' : 'Belgilash'}
            </button>
            {options.length > 2 && (
              <button onClick={() => setOptions(prev => prev.filter((_, i) => i !== idx))} className="w-full text-xs text-red-400 hover:text-red-600 transition">
                O'chirish
              </button>
            )}
          </div>
        ))}
        <button
          onClick={() => setOptions(prev => [...prev, { imageUrl: null, label: '', isCorrect: false }])}
          className="aspect-square rounded-xl border-2 border-dashed border-gray-200 flex flex-col items-center justify-center text-gray-400 hover:border-primary-400 hover:text-primary-600 transition"
        >
          <Plus className="w-6 h-6" />
          <span className="text-xs mt-1">Qo'shish</span>
        </button>
      </div>
      <EditorFooter activity={activity} onSave={handleSave} onDelete={onDelete} saving={saving} />
    </div>
  );
}

function AudioPicker({
  audioUrl,
  uploading,
  compact = false,
  onPick,
  onClear,
}: {
  audioUrl: string;
  uploading: boolean;
  compact?: boolean;
  onPick: (file: File) => void;
  onClear: () => void;
}) {
  if (uploading) {
    return (
      <div className={`flex items-center justify-center ${compact ? 'h-10' : 'h-14'} rounded-lg bg-gray-50 border`}>
        <Loader2 className="w-4 h-4 animate-spin text-primary-600" />
      </div>
    );
  }
  if (audioUrl) {
    return (
      <div className={`flex items-center gap-2 ${compact ? 'p-1.5' : 'p-2'} rounded-lg border bg-white`}>
        <audio controls src={audioUrl} className="flex-1 h-8" />
        <label className="p-1.5 rounded hover:bg-gray-100 cursor-pointer transition" title="Almashtirish">
          <Upload className="w-4 h-4 text-gray-500" />
          <input type="file" className="sr-only" accept="audio/*" onChange={e => e.target.files?.[0] && onPick(e.target.files[0])} />
        </label>
        <button onClick={onClear} className="p-1.5 rounded hover:bg-red-50 text-gray-400 hover:text-red-500 transition" title="O'chirish">
          <Trash2 className="w-4 h-4" />
        </button>
      </div>
    );
  }
  return (
    <label className={`flex items-center justify-center gap-2 ${compact ? 'h-10 text-xs' : 'h-14 text-sm'} rounded-lg border-2 border-dashed border-gray-200 text-gray-400 hover:border-primary-400 hover:text-primary-600 cursor-pointer transition`}>
      <Volume2 className="w-4 h-4" />
      Audio yuklash
      <input type="file" className="sr-only" accept="audio/*" onChange={e => e.target.files?.[0] && onPick(e.target.files[0])} />
    </label>
  );
}

function EditorFooter({ activity, onSave, onDelete, saving }: { activity?: Activity; onSave: () => void; onDelete: () => void; saving: boolean }) {
  return (
    <div className="flex items-center justify-between pt-4 border-t mt-4">
      {activity ? (
        <button onClick={onDelete} className="text-sm text-red-500 hover:text-red-700 flex items-center gap-1 transition">
          <Trash2 className="w-4 h-4" /> Faoliyatni o'chirish
        </button>
      ) : <div />}
      <button onClick={onSave} disabled={saving} className="px-4 py-2 bg-primary-600 text-white rounded-xl flex items-center gap-2 hover:bg-primary-700 transition disabled:opacity-60">
        {saving && <Loader2 className="w-4 h-4 animate-spin" />}
        {saving ? 'Saqlanmoqda...' : activity ? 'Yangilash' : 'Yaratish'}
      </button>
    </div>
  );
}
