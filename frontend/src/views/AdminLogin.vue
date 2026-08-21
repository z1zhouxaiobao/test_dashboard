<template>
  <div class="admin-login">
    <div class="bg"></div>
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>
    <div class="orb orb-3"></div>

    <div class="lang-wrap">
      <LangSwitcher variant="login" />
    </div>

    <div class="shell">
      <section class="hero">
        <div class="logo-row">
          <img src="/logo.svg" alt="LIQUICOOL" class="login-logo" />
          <div class="logo-cn">{{ t('adminHub') }}</div>
        </div>
        <h2>{{ t('adminHeroTitle1') }}<br />{{ t('adminHeroTitle2') }}</h2>
        <p>{{ t('adminHeroDesc') }}</p>
        <div class="stats">
          <div>
            <strong>PUE &lt; 1.06</strong>
            <span>{{ t('statPueLabel') }}</span>
          </div>
          <div>
            <strong>24 / 7</strong>
            <span>{{ t('stat247') }}</span>
          </div>
          <div>
            <strong>AIoT</strong>
            <span>{{ t('statAiot') }}</span>
          </div>
        </div>
      </section>

      <section class="panel">
        <div class="panel-head">
          <h1>{{ t('adminLoginTitle') }}</h1>
          <p>{{ t('adminLoginSub') }}</p>
        </div>
        <el-form ref="formRef" :model="form" :rules="rules" @submit.prevent="handleLogin">
          <el-form-item prop="username">
            <el-input v-model="form.username" :placeholder="t('adminLoginAccount')" size="large" prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              :placeholder="t('pleaseInputPwd')"
              size="large"
              prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-button class="submit" type="primary" size="large" :loading="loading" @click="handleLogin">
            {{ t('adminLoginBtn') }}
          </el-button>
        </el-form>
        <button class="quick" type="button" @click="quickLogin('admin', '123456')">
          <span>{{ t('adminQuickLogin') }}</span>
          <b>admin / 123456</b>
        </button>
        <div class="links">
          <router-link to="/portal/home">← {{ t('backToPortal') }}</router-link>
          <a href="http://localhost:8080/swagger-ui.html" target="_blank">{{ t('apiDocs') }}</a>
        </div>
      </section>
    </div>
    <CodeLocator page="AdminLogin" />
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useI18n } from '@/composables/useI18n'
import CodeLocator from '@/components/CodeLocator.vue'
import LangSwitcher from '@/components/LangSwitcher.vue'

const { t } = useI18n()
const router = useRouter()
const auth = useAuthStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = computed(() => ({
  username: [{ required: true, message: t('adminLoginAccount'), trigger: 'blur' }],
  password: [{ required: true, message: t('pleaseInputPwd'), trigger: 'blur' }]
}))

async function doLogin(credentials) {
  loading.value = true
  try {
    const user = await auth.login(credentials)
    if (user?.role !== 'ADMIN') {
      auth.logout()
      ElMessage.error(t('notAdmin'))
      return
    }
    ElMessage.success(t('welcomeBack'))
    router.push('/admin/dashboard')
  } catch {
    /* handled */
  } finally {
    loading.value = false
  }
}

async function handleLogin() {
  await formRef.value?.validate()
  await doLogin({ ...form })
}

function quickLogin(username, password) {
  form.username = username
  form.password = password
  doLogin({ username, password })
}
</script>

<style scoped>
.admin-login {
  min-height: 100vh;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 20px;
  font-family: "Segoe UI", "PingFang SC", "Microsoft YaHei", sans-serif;
}
.lang-wrap {
  position: absolute;
  top: 24px;
  right: 24px;
  z-index: 20;
}
.bg {
  position: absolute;
  inset: 0;
  background:
    linear-gradient(120deg, rgba(6, 16, 38, 0.72), rgba(11, 94, 215, 0.38)),
    url('/images/login-bg.jpg') center/cover no-repeat;
  transform: scale(1.04);
  filter: saturate(1.15);
}
.orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(40px);
  pointer-events: none;
}
.orb-1 {
  width: 280px;
  height: 280px;
  background: rgba(11, 94, 215, 0.45);
  top: -60px;
  left: -40px;
}
.orb-2 {
  width: 220px;
  height: 220px;
  background: rgba(80, 200, 255, 0.28);
  bottom: 40px;
  right: 8%;
}
.orb-3 {
  width: 160px;
  height: 160px;
  background: rgba(220, 53, 69, 0.22);
  top: 18%;
  right: 22%;
}
.shell {
  position: relative;
  z-index: 2;
  width: min(980px, 100%);
  display: grid;
  grid-template-columns: 1.05fr 0.95fr;
  min-height: 540px;
  border-radius: 28px;
  overflow: hidden;
  border: 1px solid rgba(255, 255, 255, 0.18);
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.45);
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(22px);
}
.hero {
  padding: 48px 44px;
  color: #fff;
  background: linear-gradient(180deg, rgba(10, 28, 68, 0.35), rgba(8, 18, 40, 0.55));
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.logo-row {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 8px;
}
.login-logo {
  display: block;
  height: 40px;
  width: auto;
  max-width: 240px;
  filter: brightness(0) invert(1);
}
.logo-cn {
  font-size: 12px;
  opacity: 0.78;
}
.hero h2 {
  margin: 36px 0 16px;
  font-size: 34px;
  line-height: 1.25;
  font-weight: 800;
}
.hero p {
  margin: 0;
  max-width: 360px;
  line-height: 1.8;
  color: rgba(255, 255, 255, 0.82);
}
.stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
  margin-top: 40px;
}
.stats div {
  padding: 14px 10px;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
}
.stats strong {
  display: block;
  font-size: 16px;
}
.stats span {
  display: block;
  margin-top: 4px;
  font-size: 12px;
  opacity: 0.7;
}
.panel {
  padding: 48px 42px;
  background: rgba(255, 255, 255, 0.92);
}
.panel-head {
  margin-bottom: 28px;
}
.panel-head h1 {
  margin: 0 0 8px;
  font-size: 26px;
  color: #0a1628;
}
.panel-head p {
  margin: 0;
  color: #6b7280;
  font-size: 14px;
}
.panel :deep(.el-input__wrapper) {
  border-radius: 12px;
  box-shadow: 0 0 0 1px #e5eaf3 inset;
  padding: 4px 12px;
  background: #f7f9fc;
}
.panel :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #0B5ED7 inset;
}
.submit {
  width: 100%;
  height: 46px;
  border: none;
  border-radius: 12px;
  margin-top: 6px;
  font-size: 16px;
  letter-spacing: 4px;
  background: linear-gradient(90deg, #0B5ED7, #3d8bff) !important;
  box-shadow: 0 10px 24px rgba(11, 94, 215, 0.35);
}
.submit:hover {
  filter: brightness(1.06);
}
.quick {
  margin-top: 16px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 14px;
  border-radius: 12px;
  border: 1px dashed #c9d7ee;
  background: #f4f8ff;
  color: #35507a;
  cursor: pointer;
  transition: 0.2s;
}
.quick:hover {
  border-color: #0B5ED7;
  background: #ebf3ff;
}
.quick b {
  color: #0B5ED7;
}
.links {
  margin-top: 22px;
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
.links a {
  color: #4b6288;
}
.links a:hover {
  color: #0B5ED7;
}
@media (max-width: 860px) {
  .shell {
    grid-template-columns: 1fr;
  }
  .hero {
    display: none;
  }
}
</style>
