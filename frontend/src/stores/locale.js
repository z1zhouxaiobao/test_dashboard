import { defineStore } from 'pinia'
import { messages } from '@/i18n/messages'

export const useLocaleStore = defineStore('locale', {
  state: () => ({
    locale: localStorage.getItem('liquicool_locale') || 'zh-CN'
  }),
  getters: {
    localeLabel(state) {
      const map = { 'zh-CN': '简', 'zh-TW': '繁', en: 'EN' }
      return map[state.locale] || '简'
    },
    langButtonText(state) {
      const map = {
        'zh-CN': 'CN/ZH',
        'zh-TW': 'TW/ZH',
        en: 'EN'
      }
      return map[state.locale] || 'CN/ZH'
    }
  },
  actions: {
    setLocale(locale) {
      if (!messages[locale]) return
      this.locale = locale
      localStorage.setItem('liquicool_locale', locale)
      document.documentElement.lang = locale === 'en' ? 'en' : 'zh'
    },
    t(key) {
      const pack = messages[this.locale] || messages['zh-CN']
      return pack[key] || messages['zh-CN'][key] || key
    }
  }
})
