import { ref, computed } from 'vue'
import { contactSettingsApi } from '@/api'
import { resolveMediaUrl } from '@/utils/media'

const logoUrl = ref('')
let loaded = false
let loading = null

export function useSiteLogo() {
  const siteLogo = computed(() => resolveMediaUrl(logoUrl.value) || '/logo.svg')

  async function loadSiteLogo(force = false) {
    if (!force && loaded) return
    if (loading) return loading
    loading = (async () => {
      try {
        const res = await contactSettingsApi.portal()
        const data = res.data || res || {}
        logoUrl.value = data.logoUrl || ''
        loaded = true
      } catch {
        logoUrl.value = ''
        loaded = true
      } finally {
        loading = null
      }
    })()
    return loading
  }

  function setSiteLogo(url) {
    logoUrl.value = url || ''
    loaded = true
  }

  return { siteLogo, loadSiteLogo, setSiteLogo }
}
