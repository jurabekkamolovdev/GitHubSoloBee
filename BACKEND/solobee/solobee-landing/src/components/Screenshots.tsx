const mockups = [
  {
    src: '/screens/onboarding.png',
    title: 'Onboarding',
    desc: 'Learn English with Fun! — bola ilovaga qiziqib kirishadi.',
  },
  {
    src: '/screens/category.png',
    title: 'Kategoriyalar',
    desc: 'Basics, Food, World va boshqalar — 8 asosiy kategoriya.',
  },
  {
    src: '/screens/alphabet.png',
    title: 'Alifbo',
    desc: 'Har bir harf bosqichma-bosqich — A ochilgach B.',
  },
  {
    src: '/screens/game.png',
    title: 'O‘yin va mashqlar',
    desc: 'Learn, Writing, WordHunt, PicQuest — 3+ xil o‘yin turi.',
  },
]

export default function Screenshots() {
  return (
    <section id="screenshots" className="py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="section-title">Ilova ichida</h2>
          <p className="mt-4 text-lg text-brand-muted">
            Har bir ekran bolangiz uchun o‘ylab tuzilgan — yorqin, soda, qiziqarli.
          </p>
        </div>

        <div className="mt-16 grid gap-8 sm:grid-cols-2 lg:grid-cols-4">
          {mockups.map((m) => (
            <div key={m.title} className="group flex flex-col items-center">
              <div className="relative rounded-[2.2rem] bg-brand-dark p-2 shadow-pop transition group-hover:-translate-y-2">
                <div className="relative h-[400px] w-[200px] overflow-hidden rounded-[1.8rem] bg-white">
                  <img
                    src={m.src}
                    alt={m.title}
                    className="h-full w-full object-cover"
                    loading="lazy"
                  />
                </div>
              </div>
              <h3 className="mt-5 font-display text-lg font-extrabold text-brand-dark">{m.title}</h3>
              <p className="mt-1 max-w-[220px] text-center text-sm text-brand-muted">{m.desc}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
