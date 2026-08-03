import { Phone, Mail } from 'lucide-react'

export default function CTASection() {
  return (
    <section id="cta" className="py-20 md:py-28">
      <div className="mx-auto max-w-5xl px-5 sm:px-8">
        <div className="relative overflow-hidden rounded-[2.5rem] bg-gradient-to-br from-brand-yellow via-brand-orange to-brand-red p-10 text-center shadow-pop md:p-16">
          <div className="absolute -left-10 -top-10 h-40 w-40 rounded-full bg-white/20 blur-2xl" />
          <div className="absolute -bottom-16 -right-10 h-56 w-56 rounded-full bg-white/15 blur-3xl" />

          <img
            src="/logo.png"
            alt=""
            className="mx-auto h-24 w-24 drop-shadow-xl animate-float"
          />

          <span className="mt-6 inline-block rounded-full bg-white/20 px-4 py-1.5 text-sm font-bold text-white backdrop-blur">
            Ilova tez kunda ishga tushadi
          </span>

          <h2 className="mt-4 font-display text-3xl font-extrabold text-white sm:text-4xl md:text-5xl">
            SoloBee’dan xabardor bo‘lib turing!
          </h2>
          <p className="mx-auto mt-4 max-w-xl text-white/90 md:text-lg">
            Bog‘cha yoki ta’lim muassasangiz uchun SoloBee bilan hamkorlik — to‘g‘ridan-to‘g‘ri biz bilan bog‘laning.
          </p>

          <div className="mt-8 flex flex-col items-center justify-center gap-3 sm:flex-row">
            <a
              href="tel:+998918101506"
              className="inline-flex items-center gap-2 rounded-full bg-brand-dark px-7 py-3.5 font-bold text-white shadow-lg transition hover:-translate-y-0.5"
            >
              <Phone size={18} />
              +998 91 810 15 06
            </a>
            <a
              href="mailto:xurshidakhan06@gmail.com"
              className="inline-flex items-center gap-2 rounded-full bg-white px-7 py-3.5 font-bold text-brand-dark shadow-lg transition hover:-translate-y-0.5"
            >
              <Mail size={18} />
              Email yuborish
            </a>
          </div>

          {/* TEMP: Store download buttons — ilova chiqqandan keyin qaytariladi
          <div className="mt-8 flex flex-col items-center justify-center gap-3 sm:flex-row">
            <a href="#" className="inline-flex items-center gap-2 rounded-full bg-brand-dark px-7 py-3.5 font-bold text-white shadow-lg transition hover:-translate-y-0.5">
              <Play size={20} />
              Google Play
            </a>
            <a href="#" className="inline-flex items-center gap-2 rounded-full bg-white px-7 py-3.5 font-bold text-brand-dark shadow-lg transition hover:-translate-y-0.5">
              <Apple size={20} />
              App Store
            </a>
          </div>
          */}
        </div>
      </div>
    </section>
  )
}
