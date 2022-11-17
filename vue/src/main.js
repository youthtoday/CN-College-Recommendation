import {
    createApp
} from 'vue';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import App from './App.vue';
import './index.css';
import router from './router'

const app = createApp(App);
// app.config.globalProperties.$axios = axios;
// app.defaults.baseURL = 'http://101.132.242.168:8080/user/'
app.use(router)
app.use(ElementPlus)
app.mount('#app')