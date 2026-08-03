import { useEffect, useState } from 'react'
import { Menu, X } from 'lucide-react'
import clsx from 'clsx'

const links = [
  { href: '#features', label: 'Xususiyatlar' },
  { href: '#how', label: 'Qanday ishlaydi' },
  { href: '#roadmap', label: 'Yo‘l xaritasi' },
  { href: '#team', label: 'Jamoa' },
  { href: '#contact', label: 'Aloqa' },
]

export default function Navbar() {
  const [scrolled, setScrolled] = useState(false)
  const [open, setOpen] = useState(false)

  useEffect(() => {
    const onScroll = () => setScrolled(window.scrollY > 12)
    onScroll()
    window.addEventListener('scroll', onScroll)
    return () => window.removeEventListener('scroll', onScroll)
  }, [])

  return (
    <header
      className={clsx(
        'sticky top-0 z-50 w-full transition-all',
        scrolled
          ? 'bg-bg-base/85 backdrop-blur border-b border-brand-dark/5 shadow-sm'
          : 'bg-transparent',
      )}
    >
      <div className="mx-auto flex max-w-7xl items-center justify-between px-5 py-3 sm:px-8">
        <a href="#top" className="flex items-center gap-2.5">
          <img src="/logo.png" alt="SoloBee" className="h-11 w-11 object-contain" />
          <span className="font-display text-2xl font-extrabold text-brand-yellowDeep">
            SoloBee
          </span>
        </a>

        <nav className="hidden items-center gap-8 md:flex">
          {links.map((l) => (
            <a
              key={l.href}
              href={l.href}
              className="font-semibold text-brand-dark/80 transition hover:text-brand-orange"
            >
              {l.label}
            </a>
          ))}
        </nav>

        {/* TEMP: CTA tugmalar vaqtincha olib qo‘yildi — ilova chiqqandan keyin qaytariladi
        <a href="#cta" className="btn-primary hidden md:inline-flex">
          <Download size={18} />
          Ilovani olish
        </a>
        <a href="#contact" className="btn-primary hidden md:inline-flex">
          Biz bilan bog‘laning
        </a>
        */}

        <button
          className="md:hidden rounded-full p-2 text-brand-dark"
          onClick={() => setOpen((v) => !v)}
          aria-label="menu"
        >
          {open ? <X size={26} /> : <Menu size={26} />}
        </button>
      </div>

      {open && (
        <div className="md:hidden border-t border-brand-dark/5 bg-bg-base/95 backdrop-blur">
          <div className="mx-auto flex max-w-7xl flex-col gap-1 px-5 py-4">
            {links.map((l) => (
              <a
                key={l.href}
                href={l.href}
                onClick={() => setOpen(false)}
                className="rounded-xl px-3 py-2.5 font-semibold text-brand-dark hover:bg-brand-cream"
              >
                {l.label}
              </a>
            ))}
            {/* TEMP: mobile CTA vaqtincha olib qo‘yildi
            <a
              href="#contact"
              onClick={() => setOpen(false)}
              className="btn-primary mt-2 justify-center"
            >
              Biz bilan bog‘laning
            </a>
            */}
          </div>
        </div>
      )}
    </header>
  )
}
