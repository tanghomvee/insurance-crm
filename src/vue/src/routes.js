import Login from './views/login.vue';
import NotFound from './views/404.vue';
import Home from './views/home.vue';
import Main from './views/main.vue';
import users from './views/user/list.vue';
import acct from './views/acct/list.vue';
import catg from './views/catg/list.vue';
import record from './views/record/list.vue';
import data from './views/data/list.vue';
import userData from './views/data/userData.vue';
import settings from './views/user/settings.vue';
import Form from './views/content/Form.vue';
import user from './views/content/user.vue';
import echarts from './views/charts/echarts.vue';

let routes = [
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },

    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '/',
        component: Home,
        name: '',
        hidden: true,
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/user/setting', component: settings, name: '账户设置' },
        ]
    },
    {
        path: '/',
        component: Home,
        name: '用户管理',
        iconCls: 'fa fa-use',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/user/list', component: users, name: '用户列表' },
           /* { path: '/form', component: Form, name: 'Form' },
            { path: '/user', component: user, name: '列表' },*/
        ]
    },
    {
        path: '/',
        component: Home,
        name: '账户管理',
        iconCls: 'fa fa-google-wallet',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/acct/list', component: acct, name: '账户列表' }

        ]
    },
    {
        path: '/',
        component: Home,
        name: '数据管理',
        iconCls: 'fa fa-users',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/data/list', component: data, name: '数据列表' },
            { path: '/data/userData', component: userData, name: '数据匹配' },

        ]
    },
    {
        path: '/',
        component: Home,
        name: '消费记录',
        iconCls: 'fa fa-home',//图标样式class
        children: [
            { path: '/main', component: Main, name: '主页', hidden: true },
            { path: '/record/list', component: record, name: '消费列表' }

        ]
    },
  /*  {
        path: '/',
        component: Home,
        name: 'Charts',
        iconCls: 'fa fa-bar-chart',
        children: [
            { path: '/echarts', component: echarts, name: 'echarts' }
        ]
    },*/
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
];

export default routes;