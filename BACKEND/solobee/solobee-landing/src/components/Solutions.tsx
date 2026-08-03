import { CheckCircle2, Sparkles } from 'lucide-react'

const items = [
  {
    title: 'Fonetik metodika',
    text: 'Tovush → harf → bo‘g‘in → so‘z → gap ketma-ketligida tez va samarali o‘rganish.',
  },
  {
    title: 'Interaktiv o‘qitish',
    text: 'Ham o‘zbek, ham ingliz tillarida interaktiv darslar va o‘yinlar.',
  },
  {
    title: 'O‘yin tarzida',
    text: 'Bolalar uchun qiziqarli mini-o‘yinlar — mashqlar zerikarli emas.',
  },
  {
    title: 'Saralangan videolar',
    text: 'Alifbo, ertaklar va ingliz tilidagi oddiy darajadagi multi-videolar to‘plami.',
  },
]

export default function Solutions() {
  return (
    <section id="features" className="relative bg-bg-soft py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <span className="inline-flex items-center gap-2 rounded-full bg-emerald-500/10 px-4 py-1.5 text-sm font-bold text-emerald-700">
            <Sparkles size={16} /> Yechimlar
          </span>
          <h2 className="section-title mt-4">SoloBee nima taklif qiladi?</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Fonetik metodika, o‘yinlar va multimedia — hammasi bir ilovada.
          </p>
        </div>

        <div className="mt-14 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {items.map((it) => (
            <div key={it.title} className="card">
              <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-2xl bg-emerald-500/15 text-emerald-600">
                <CheckCircle2 size={26} strokeWidth={2.5} />
              </div>
              <h3 className="font-display text-xl font-extrabold text-brand-dark">{it.title}</h3>
              <p className="mt-2 text-brand-muted leading-relaxed">{it.text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
