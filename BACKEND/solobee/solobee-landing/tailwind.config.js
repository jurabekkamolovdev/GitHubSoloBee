/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          yellow: '#FFC83D',
          yellowDeep: '#F5A623',
          orange: '#F39C12',
          red: '#E74C3C',
          blue: '#4A90E2',
          blueSoft: '#BCE2FF',
          cream: '#FFF8E7',
          dark: '#2C3E50',
          muted: '#6B7A8F',
        },
        bg: {
          base: '#FFF9EC',
          soft: '#FFF3D6',
        }
      },
      fontFamily: {
        display: ['"Baloo 2"', 'system-ui', 'sans-serif'],
        sans: ['Nunito', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'soft': '0 10px 30px -10px rgba(44, 62, 80, 0.15)',
        'pop': '0 20px 40px -15px rgba(243, 156, 18, 0.35)',
      },
      borderRadius: {
        '4xl': '2rem',
      },
      keyframes: {
        float: {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-12px)' },
        },
        wiggle: {
          '0%, 100%': { transform: 'rotate(-3deg)' },
          '50%': { transform: 'rotate(3deg)' },
        },
      },
      animation: {
        float: 'float 4s ease-in-out infinite',
        wiggle: 'wiggle 3s ease-in-out infinite',
      },
    },
  },
  plugins: [],
}
