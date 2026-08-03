const team = [
  {
    name: 'Xurshida Kuchkarova',
    role: 'CEO',
    bio: 'Startup Garage residenti, Founders School bitiruvchisi, SHDPI Xorijiy til va adabiyoti yo‘nalishi talabasi.',
    color: 'from-brand-orange to-brand-yellow',
    image: '/team/Xurshida.jpg',
  },
  {
    name: 'Bexzod G‘ayratov',
    role: 'CTO / Backend',
    bio: '4 yillik tajribaga ega Flutter Muhandisi. Full-stack dasturlash, UI/UX dizayn va miqyosli yechimlar bo‘yicha ixtisoslashgan.',
    color: 'from-brand-blue to-purple-500',
    image: '/team/Bekzod.jpg',
  },
  {
    name: 'Javohir Oromov',
    role: 'CTO UI/UX · Mobil dasturchi',
    bio: 'Dasturchi, ishlab chiquvchi — Java, Kotlin, SQL, Android Studio, CMP, MVI, MVVM, MVP, Git, Android SDK, KMP.',
    color: 'from-emerald-400 to-brand-blue',
    image: '/team/Javohir.jpg',
  },
]

export default function Team() {
  return (
    <section id="team" className="py-20 md:py-28">
      <div className="mx-auto max-w-7xl px-5 sm:px-8">
        <div className="mx-auto max-w-2xl text-center">
          <h2 className="section-title">Jamoa</h2>
          <p className="mt-4 text-lg text-brand-muted">
            SoloBee ortida yosh va tajribali mutaxassislar turadi.
          </p>
        </div>

        <div className="mt-14 grid gap-6 md:grid-cols-3">
          {team.map((t) => (
            <div key={t.name} className="card text-center">
              {t.image ? (
                <img
                  src={t.image}
                  alt={t.name}
                  className="mx-auto h-24 w-24 rounded-full object-cover shadow-pop"
                />
              ) : (
                <div
                  className={`mx-auto flex h-24 w-24 items-center justify-center rounded-full bg-gradient-to-br ${t.color} font-display text-3xl font-extrabold text-white shadow-pop`}
                >
                  {initials(t.name)}
                </div>
              )}
              <h3 className="mt-5 font-display text-xl font-extrabold text-brand-dark">{t.name}</h3>
              <div className="mt-1 text-sm font-bold text-brand-orange">{t.role}</div>
              <p className="mt-3 text-sm leading-relaxed text-brand-muted">{t.bio}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}

function initials(name: string) {
  return name
    .split(' ')
    .map((p) => p[0])
    .slice(0, 2)
    .join('')
    .toUpperCase()
}
