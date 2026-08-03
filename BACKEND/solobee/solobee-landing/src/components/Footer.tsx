import { Phone, Mail } from 'lucide-react'

export default function Footer() {
  return (
    <footer id="contact" className="bg-brand-dark text-white">
      <div className="mx-auto grid max-w-7xl gap-10 px-5 py-16 sm:px-8 md:grid-cols-3">
        <div>
          <div className="flex items-center gap-2.5">
            <img src="/logo.png" alt="SoloBee" className="h-12 w-12 rounded-xl bg-white/5 p-1" />
            <span className="font-display text-2xl font-extrabold text-brand-yellow">SoloBee</span>
          </div>
          <p className="mt-4 max-w-sm text-sm leading-relaxed text-white/70">
            Bolalar uchun qiziqarli ingliz tili o‘quv ilovasi. O‘yinlar, tovushlar va rangli
            tasvirlar orqali savod chiqarish.
          </p>
        </div>

        <div>
          <h4 className="font-display text-lg font-extrabold">Navigatsiya</h4>
          <ul className="mt-4 space-y-2 text-sm text-white/70">
            <li><a href="#features" className="hover:text-brand-yellow">Xususiyatlar</a></li>
            <li><a href="#how" className="hover:text-brand-yellow">Qanday ishlaydi</a></li>
            <li><a href="#roadmap" className="hover:text-brand-yellow">Yo‘l xaritasi</a></li>
            <li><a href="#team" className="hover:text-brand-yellow">Jamoa</a></li>
          </ul>
        </div>

        <div>
          <h4 className="font-display text-lg font-extrabold">Aloqa</h4>
          <ul className="mt-4 space-y-3 text-sm text-white/80">
            <li className="flex items-center gap-3">
              <Phone size={16} className="text-brand-yellow" />
              <a href="tel:+998918101506" className="hover:text-brand-yellow">+998 91 810 15 06</a>
            </li>
            <li className="flex items-center gap-3">
              <Mail size={16} className="text-brand-yellow" />
              <a href="mailto:xurshidakhan06@gmail.com" className="hover:text-brand-yellow">
                xurshidakhan06@gmail.com
              </a>
            </li>
          </ul>

          {/* TEMP: ijtimoiy tarmoq iconlari — linklar tayyor bo‘lgach qaytariladi
          <div className="mt-5 flex gap-3">
            <SocialLink href="#" icon={<Instagram size={18} />} />
            <SocialLink href="#" icon={<Send size={18} />} />
            <SocialLink href="#" icon={<Youtube size={18} />} />
          </div>
          */}
        </div>
      </div>

      <div className="border-t border-white/10">
        <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-2 px-5 py-6 text-xs text-white/50 sm:px-8 md:flex-row">
          <span>© {new Date().getFullYear()} SoloBee. Barcha huquqlar himoyalangan.</span>
          <span>solobee.uz</span>
        </div>
      </div>
    </footer>
  )
}

/* TEMP: ijtimoiy tarmoq iconlari uchun helper — linklar tayyor bo‘lgach qaytariladi
function SocialLink({ href, icon }: { href: string; icon: React.ReactNode }) {
  return (
    <a
      href={href}
      className="flex h-10 w-10 items-center justify-center rounded-full bg-white/10 transition hover:bg-brand-yellow hover:text-brand-dark"
    >
      {icon}
    </a>
  )
}
*/
