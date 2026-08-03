import { BookOpen, Gamepad2, Layers, Volume2, Video, TrendingUp, Languages, Trophy } from 'lucide-react'

const features = [
  {
    icon: Languages,
    title: '2 tilda o‘qitish',
    text: 'Ingliz va o‘zbek tillarida alifbo, so‘zlar va gaplar. Bola bir vaqtning o‘zida ikki tilni mustahkamlaydi.',
    color: 'text-brand-blue',
    bg: 'bg-brand-blue/10',
  },
  {
    icon: BookOpen,
    title: 'Fonetik metodika',
    text: '“A” harfi to‘liq tushuntirilib, o‘yinlar orqali mustahkamlangandan keyin “B” harfi ochiladi.',
    color: 'text-brand-red',
    bg: 'bg-brand-red/10',
  },
  {
    icon: Gamepad2,
    title: '3 xil o‘yin turi',
    text: 'Har bir harf yoki so‘zni uchta xilma-xil o‘yin orqali mustahkamlash imkoniyati.',
    color: 'text-brand-orange',
    bg: 'bg-brand-orange/10',
  },
  {
    icon: Layers,
    title: '8 kategoriya + 30 sub-kategoriya',
    text: 'World, Feelings & Actions, Time & Nature, Food va boshqalar — keng qamrovli kontent.',
    color: 'text-brand-yellowDeep',
    bg: 'bg-brand-yellow/20',
  },
  {
    icon: Volume2,
    title: 'Tovush va talaffuz',
    text: 'Har bir harf va so‘z sifatli audio bilan — to‘g‘ri talaffuzni boshidan o‘rganish.',
    color: 'text-purple-600',
    bg: 'bg-purple-500/10',
  },
  {
    icon: Video,
    title: 'Saralangan videolar',
    text: 'Alifbo, ertaklar va ingliz tilidagi oddiy darajadagi multi-videolar to‘plami.',
    color: 'text-pink-600',
    bg: 'bg-pink-500/10',
  },
  {
    icon: TrendingUp,
    title: 'Progress kuzatuvi',
    text: 'Bola qaysi darsda, nima o‘rgandi — ota-ona statistikani real vaqtda ko‘radi.',
    color: 'text-emerald-600',
    bg: 'bg-emerald-500/10',
  },
  {
    icon: Trophy,
    title: 'Mustahkamlovchi mashqlar',
    text: 'Har bir bosqich oxirida mini-imtihon — o‘rgangan bilim mustahkam bo‘ladi.',
    color: 'text-amber-600',
    bg: 'bg-amber-500/10',
  },
]

export default function Features() {
  return (
    <section id="features-full" className="py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="section-title">Nima uchun aynan SoloBee?</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Biz bolalarga o‘qishni qiziqarli qiladigan barcha elementlarni bir joyga jamladik.
          </p>
        </div>

        <div className="mt-14 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {features.map(({ icon: Icon, ...f }) => (
            <div key={f.title} className="card">
              <div className={`mb-4 flex h-12 w-12 items-center justify-center rounded-2xl ${f.bg} ${f.color}`}>
                <Icon size={24} strokeWidth={2.2} />
              </div>
              <h3 className="font-display text-lg font-extrabold text-brand-dark">{f.title}</h3>
              <p className="mt-2 text-sm leading-relaxed text-brand-muted">{f.text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
