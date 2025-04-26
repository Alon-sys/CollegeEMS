import Vue from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/global.css'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
// main.js
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn' // 如果需要中文支持


// 设置为中文
dayjs.locale('zh-cn')

Vue.config.productionTip = false
Vue.use(ElementUI)
Vue.use(ElementUI, { size: "small" });
Vue.prototype.$dayjs = dayjs



new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
