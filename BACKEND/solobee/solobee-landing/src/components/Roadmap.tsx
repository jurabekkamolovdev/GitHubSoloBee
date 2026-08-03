const milestones = [
  {
    year: '2026',
    region: 'Shahrisabz',
    text: 'Davlat va xususiy bog‘chalar bilan B2B shartnomalar. Metodikani amaliyotda sinash — haftasiga 3 tadan bog‘cha.',
  },
  {
    year: '2027',
    region: 'Qashqadaryo',
    text: 'Viloyat qamrovi: Qashqadaryoning barcha tumanlariga kengayish. Ingliz tili moduli to‘liq integratsiya.',
  },
  {
    year: '2028',
    region: 'O‘zbekiston',
    text: 'Milliy miqyos: Respublika bo‘ylab ta’lim muassasalariga kirish. MMTV bilan hamkorlikni yo‘lga qo‘yish.',
  },
  {
    year: '2029',
    region: 'Innovatsiya',
    text: 'AI va shaxsiylashtirish: bolaning o‘zlashtirishiga qarab sun’iy intellekt algoritmlari. B2C (ota-onalar uchun).',
  },
  {
    year: '2030',
    region: 'Xalqaro bozor',
    text: 'Qardosh tillar uchun mahsulotni adaptatsiya. SoloBee brendi ostida onlayn maktab va jismoniy ta’lim vositalari.',
  },
]

export default function Roadmap() {
  return (
    <section id="roadmap" className="bg-bg-soft py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="section-title">Yo‘l xaritasi · 2026 → 2030</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Shahrisabzdan boshlab xalqaro bozorgacha — aniq va izchil strategiya.
          </p>
        </div>

        <div className="mt-16 space-y-4">
          {milestones.map((m, i) => (
            <div
              key={m.year}
              className="grid items-center gap-4 rounded-3xl bg-white p-6 shadow-soft ring-1 ring-brand-dark/5 md:grid-cols-[120px_220px_1fr]"
            >
              <div className="font-display text-3xl font-extrabold text-brand-orange">{m.year}</div>
              <div className="flex items-center gap-3">
                <div className={`h-3 w-3 rounded-full ${dotColor(i)}`} />
                <span className="font-bold text-brand-dark">{m.region}</span>
              </div>
              <p className="text-brand-muted leading-relaxed">{m.text}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

function dotColor(i: number) {
  return [
    'bg-brand-blue',
    'bg-brand-orange',
    'bg-emerald-500',
    'bg-purple-500',
    'bg-brand-red',
  ][i % 5]
}
