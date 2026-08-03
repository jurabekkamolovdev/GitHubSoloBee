import { HelpCircle } from 'lucide-react'

const items = [
  'Bog‘chalarda tayyorlov kursidagi bolalarda aniq bir qo‘llanmaning mavjud emasligi',
  'Bozorda to‘liq moslashgan interaktiv savod chiqarish ilovasi juda ham oz',
  'Bolalarni qiziqtira olish qiyinchiligi va erinchoqlik',
  'Til ko‘nikmalarini o‘rganib, ularni qiziqarli, mos o‘yinlar orqali mustahkamlash',
]

export default function Problems() {
  return (
    <section id="problems" className="relative py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <span className="inline-flex items-center gap-2 rounded-full bg-brand-red/10 px-4 py-1.5 text-sm font-bold text-brand-red">
            <HelpCircle size={16} /> Muammolar
          </span>
          <h2 className="section-title mt-4">Bugungi ota-onalar nimaga duch kelmoqda?</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Kichkintoylar uchun to‘g‘ri metodika va qiziqarli vosita topish oson emas.
          </p>
        </div>

        <div className="mt-14 grid gap-5 md:grid-cols-2">
          {items.map((text, i) => (
            <div
              key={i}
              className="flex items-start gap-4 rounded-3xl bg-white p-6 shadow-soft ring-1 ring-brand-dark/5"
            >
              <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-2xl bg-brand-red/10 font-display text-xl font-extrabold text-brand-red">
                ?
              </div>
              <p className="text-brand-dark/85 leading-relaxed">{text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
