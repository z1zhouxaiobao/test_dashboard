import { computed } from 'vue'
import { storeToRefs } from 'pinia'
import { useLocaleStore } from '@/stores/locale'
import { messages } from '@/i18n/messages'
import { valueKeys } from '@/i18n/admin'

export function useI18n() {
  const localeStore = useLocaleStore()
  const { locale } = storeToRefs(localeStore)

  const dict = computed(() => messages[locale.value] || messages['zh-CN'])

  const t = (key, params) => {
    let text = dict.value[key] || messages['zh-CN'][key] || key
    if (params && typeof text === 'string') {
      Object.keys(params).forEach((k) => {
        text = text.replace(new RegExp(`\\{${k}\\}`, 'g'), String(params[k]))
      })
    }
    return text
  }

  const tv = (value) => {
    const key = valueKeys[value]
    return key ? t(key) : value
  }

  const setLocale = (next) => localeStore.setLocale(next)

  return { locale, t, tv, setLocale, localeStore, dict }
}
