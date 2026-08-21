<template>
  <div class="register-page">
    <div class="register-overlay"></div>
    <div class="register-card">
      <div class="brand">
        <img :src="siteLogo" alt="LIQUICOOL" class="login-logo" />
        <h1>注册账号</h1>
        <p>加入立冷科技 LIQUICOOL</p>
      </div>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" placeholder="请再次输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleRegister">注册</el-button>
          <el-button @click="$router.push({ path: '/login', query: $route.query })">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
    <CodeLocator page="Register" />
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
  nickname: '',
  password: '',
  confirmPassword: ''
})

const validateConfirm = (rule, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, message: '至少6位', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirm, trigger: 'blur' }]
}

function resolveRedirect() {
  const raw = typeof route.query.redirect === 'string' ? route.query.redirect : ''
  const hashRaw = typeof route.query.redirectHash === 'string' ? route.query.redirectHash : ''
  const hash = hashRaw ? (hashRaw.startsWith('#') ? hashRaw : `#${hashRaw}`) : ''
  if (raw.startsWith('/') && !raw.startsWith('//')) {
    return { path: raw, hash }
  }
  return { path: '/portal/home' }
}

async function handleRegister() {
  await formRef.value?.validate()
  loading.value = true
  try {
    await auth.register({
      username: form.username,
      nickname: form.nickname,
      password: form.password
    })
    ElMessage.success('注册成功')
    await router.push(resolveRedirect())
  } catch {
    /* handled */
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: url('/images/register-bg.jpg') center/cover no-repeat, linear-gradient(135deg, #0a1628 0%, #0B5ED7 100%);
  position: relative;
}
.register-overlay {
  position: absolute;
  inset: 0;
  background: rgba(10, 22, 40, 0.5);
}
.register-card {
  position: relative;
  z-index: 1;
  width: min(460px, calc(100vw - 32px));
  padding: 40px 36px;
  background: rgba(255, 255, 255, 0.45);
  backdrop-filter: blur(18px);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}
.brand {
  text-align: center;
  margin-bottom: 24px;
}
.login-logo {
  display: block;
  height: 36px;
  width: auto;
  max-width: 200px;
  margin: 0 auto 12px;
}
.brand h1 {
  margin: 0;
  color: #0B5ED7;
}
.brand p {
  margin: 8px 0 0;
  color: #666;
  font-size: 14px;
}
@media (max-width: 480px) {
  .register-card {
    padding: 28px 18px;
  }
}
</style>
