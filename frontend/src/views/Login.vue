<template>
  <div class="login-page">
    <div class="login-overlay"></div>
    <div class="login-card">
      <div class="brand">
        <img :src="siteLogo" alt="LIQUICOOL" class="login-logo" />
        <p>用户登录</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0" @submit.prevent="handleLogin">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" class="login-btn" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
        <div class="links">
          <router-link :to="{ path: '/register', query: $route.query }">注册账号</router-link>
          <router-link to="/portal/home">返回门户</router-link>
        </div>
      </el-form>
      <div class="quick-login">
        <span>快捷登录：</span>
        <el-button type="primary" link @click="quickLogin('user1', '123456')">用户 user1 / 123456</el-button>
      </div>
    </div>
    <CodeLocator page="Login" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores/auth'
import { useSiteLogo } from '@/composables/useSiteLogo'
import CodeLocator from '@/components/CodeLocator.vue'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const { siteLogo, loadSiteLogo } = useSiteLogo()
onMounted(loadSiteLogo)
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

function resolveRedirect(user) {
  const raw = typeof route.query.redirect === 'string' ? route.query.redirect : ''
  const hashRaw = typeof route.query.redirectHash === 'string' ? route.query.redirectHash : ''
  const hash = hashRaw ? (hashRaw.startsWith('#') ? hashRaw : `#${hashRaw}`) : ''
  if (raw.startsWith('/') && !raw.startsWith('//')) {
    return { path: raw, hash }
  }
  if (user?.role === 'ADMIN') return { path: '/admin/dashboard' }
  return { path: '/portal/home' }
}

async function doLogin(credentials) {
  loading.value = true
  try {
    const user = await auth.login(credentials)
    ElMessage.success('登录成功')
    await router.push(resolveRedirect(user))
  } catch {
    /* handled by http interceptor */
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
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url('/images/login-bg.jpg') center/cover no-repeat, linear-gradient(135deg, #0a1628 0%, #0B5ED7 100%);
  position: relative;
}
.login-overlay {
  position: absolute;
  inset: 0;
  background: rgba(10, 22, 40, 0.55);
}
.login-card {
  position: relative;
  z-index: 1;
  width: min(420px, calc(100vw - 32px));
  padding: 40px 36px;
  background: rgba(255, 255, 255, 0.45);
  backdrop-filter: blur(18px);
  border-radius: 12px;
  border: 1px solid rgba(255, 255, 255, 0.35);
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}
.brand {
  text-align: center;
  margin-bottom: 32px;
}
.login-logo {
  display: block;
  height: 40px;
  width: auto;
  max-width: 220px;
  margin: 0 auto;
}
.brand p {
  margin: 12px 0 0;
  color: #666;
  font-size: 14px;
}
.login-btn {
  width: 100%;
}
.links {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}
.quick-login {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid rgba(0, 0, 0, 0.08);
  font-size: 13px;
  color: #666;
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
}
@media (max-width: 480px) {
  .login-card {
    padding: 28px 18px;
  }
  .login-logo {
    height: 32px;
  }
}
</style>
