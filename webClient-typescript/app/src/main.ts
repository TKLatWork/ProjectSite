import Vue from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
// element ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// https://www.npmjs.com/package/jodit-vue
import 'jodit/build/jodit.min.css'
import JoditVue from 'jodit-vue';

Vue.config.productionTip = false;

Vue.use(ElementUI);
Vue.use(JoditVue)

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount('#app');
