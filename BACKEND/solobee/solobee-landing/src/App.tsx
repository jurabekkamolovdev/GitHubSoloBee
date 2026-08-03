import Navbar from './components/Navbar'
import Hero from './components/Hero'
import Problems from './components/Problems'
import Solutions from './components/Solutions'
import Features from './components/Features'
import HowItWorks from './components/HowItWorks'
import Screenshots from './components/Screenshots'
import Roadmap from './components/Roadmap'
import Team from './components/Team'
import CTASection from './components/CTASection'
import Footer from './components/Footer'

export default function App() {
  return (
    <div className="min-h-screen bg-bg-base">
      <Navbar />
      <main>
        <Hero />
        <Problems />
        <Solutions />
        <Features />
        <HowItWorks />
        <Screenshots />
        <Roadmap />
        <Team />
        <CTASection />
      </main>
      <Footer />
    </div>
  )
}
