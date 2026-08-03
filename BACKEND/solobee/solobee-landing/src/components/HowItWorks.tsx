const steps = [
  {
    n: '01',
    title: 'Ro‘yxatdan o‘ting',
    text: 'SoloBee’dan xabardor bo‘ling va bog‘cha yoki ta’lim muassasangiz uchun hamkorlik shartlarini oling.',
    color: 'bg-brand-blue',
  },
  {
    n: '02',
    title: 'Kuniga 15 daqiqa',
    text: 'Bola alifbo, so‘zlar va o‘yinlar orqali o‘rganadi. Siz statistikani kuzatib borasiz.',
    color: 'bg-brand-orange',
  },
  {
    n: '03',
    title: '40 kunda natija',
    text: 'Bolangiz mustaqil o‘qiy boshlaydi. Endi u ertak va multfilmlarni tushunadi.',
    color: 'bg-emerald-500',
  },
]

export default function HowItWorks() {
  return (
    <section id="how" className="bg-bg-soft py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="section-title">Qanday ishlaydi?</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Uch oddiy qadam — va bolangiz ingliz tilida o‘qiy boshlaydi.
          </p>
        </div>

        <div className="relative mt-16 grid gap-8 md:grid-cols-3">
          <div
            aria-hidden
            className="pointer-events-none absolute left-0 right-0 top-8 hidden h-0.5 border-t-2 border-dashed border-brand-yellow/60 md:block"
          />
          {steps.map((s) => (
            <div key={s.n} className="relative">
              <div
                className={`mx-auto flex h-16 w-16 items-center justify-center rounded-2xl ${s.color} font-display text-xl font-extrabold text-white shadow-pop`}
              >
                {s.n}
              </div>
              <div className="mt-6 rounded-3xl bg-white p-6 text-center shadow-soft ring-1 ring-brand-dark/5">
                <h3 className="font-display text-xl font-extrabold text-brand-dark">{s.title}</h3>
                <p className="mt-2 text-brand-muted leading-relaxed">{s.text}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
