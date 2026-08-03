import { Sparkles, ArrowRight } from 'lucide-react'

export default function Hero() {
  return (
    <section id="top" className="relative overflow-hidden hero-gradient pt-10 pb-24 md:pt-16 md:pb-32">
      <Sparkle className="absolute left-8 top-20 h-6 w-6 text-white/70 animate-float" />
      <Sparkle className="absolute right-12 top-32 h-8 w-8 text-white/60 animate-wiggle" />
      <Sparkle className="absolute left-1/3 top-10 h-5 w-5 text-white/80 animate-float" />
      <Sparkle className="absolute right-1/4 top-48 h-4 w-4 text-brand-yellow/70" />

      <div className="mx-auto grid max-w-7xl items-center gap-10 px-5 sm:px-8 md:grid-cols-2 md:gap-14">
        <div className="text-center md:text-left">
          <span className="inline-flex items-center gap-2 rounded-full bg-white/70 px-4 py-1.5 text-sm font-bold text-brand-yellowDeep ring-1 ring-brand-yellow/30 backdrop-blur">
            <Sparkles size={16} /> 4+ yosh bolalar uchun
          </span>

          <h1 className="mt-5 text-4xl font-extrabold leading-[1.05] text-brand-dark sm:text-5xl md:text-6xl">
            Farzandingiz <span className="text-brand-orange">4 yoshdan</span> ingliz tilini o‘qiy boshlaydi
          </h1>

          <p className="mt-5 text-lg text-brand-dark/75 md:text-xl">
            Kuniga atigi <b className="text-brand-dark">15 daqiqa</b> — <b className="text-brand-dark">40 kun</b> ichida
            bolangiz alifbo, so‘zlar va o‘qishni o‘yin tarzida o‘rganadi.
          </p>

          <div className="mt-8 flex flex-col items-center gap-3 sm:flex-row sm:justify-start">
            <a href="#features" className="btn-primary">
              Batafsil ko‘rish
              <ArrowRight size={20} />
            </a>
            <a href="#contact" className="btn-ghost">
              Biz bilan bog‘laning
            </a>
          </div>

          {/* TEMP: Store badges (Google Play / App Store) — ilova chiqqandan keyin qaytariladi
          <div className="mt-8 flex flex-col items-center gap-3 sm:flex-row sm:justify-start">
            <a href="#cta" className="btn-primary">
              <Play size={20} />
              Google Play
            </a>
            <a href="#cta" className="btn-ghost">
              <Apple size={20} />
              App Store
            </a>
          </div>
          */}

          <div className="mt-8 flex flex-wrap items-center justify-center gap-x-6 gap-y-2 text-sm text-brand-muted md:justify-start">
            <Stat value="8" label="Kategoriya" />
            <Dot />
            <Stat value="30+" label="Sub-kategoriya" />
            <Dot />
            <Stat value="2" label="Til: UZ & EN" />
          </div>
        </div>

        <div className="relative mx-auto w-full max-w-md">
          <div className="absolute -left-8 -top-8 h-24 w-24 rounded-full bg-brand-yellow/50 blur-2xl" />
          <div className="absolute -right-6 bottom-10 h-28 w-28 rounded-full bg-brand-orange/40 blur-2xl" />

          <div className="relative rounded-[2.5rem] bg-white p-3 shadow-pop ring-1 ring-brand-dark/10 animate-float">
            <div className="rounded-[2rem] bg-gradient-to-b from-brand-blueSoft via-white to-brand-cream p-6">
              <div className="mb-6 flex items-center justify-between">
                <div className="h-2 w-16 rounded-full bg-brand-yellow" />
                <span className="text-xs font-bold text-brand-muted">Skip</span>
              </div>

              <div className="flex justify-center">
                <img
                  src="/logo.png"
                  alt="SoloBee"
                  className="h-56 w-56 object-contain drop-shadow-xl animate-wiggle"
                />
              </div>

              <div className="mt-6 rounded-2xl bg-white/80 p-5 text-center ring-1 ring-brand-dark/5">
                <h3 className="font-display text-2xl font-extrabold text-brand-dark">
                  Learn English with Fun!
                </h3>
                <p className="mt-2 text-sm text-brand-muted">
                  O‘yinlar, tovushlar va rangli tasvirlar orqali.
                </p>
                <div className="mt-4 w-full rounded-full bg-brand-yellow py-3 text-center font-bold text-brand-dark shadow-md">
                  Tez kunda
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
  )
}

function Stat({ value, label }: { value: string; label: string }) {
  return (
    <div className="flex items-baseline gap-1.5">
      <span className="font-display text-xl font-extrabold text-brand-dark">{value}</span>
      <span className="font-semibold">{label}</span>
    </div>
  )
}

function Dot() {
  return <span className="h-1 w-1 rounded-full bg-brand-muted/60" />
}

function Sparkle({ className = '' }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="currentColor" className={className} aria-hidden>
      <path d="M12 2l2.39 6.61L21 11l-6.61 2.39L12 20l-2.39-6.61L3 11l6.61-2.39L12 2z" />
    </svg>
  )
}
