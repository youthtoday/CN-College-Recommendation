// 路由管理总文件
import {
    nextTick
} from 'vue';
import {
    createRouter,
    createWebHashHistory
} from 'vue-router';
import components from "./components";
const {
    Login,
    Header,
    Register,
    About,
    Main,
    University,
    Result,
    SubjectRanking,
    SoftRanking,
    MajorRanking
} = components;
const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
            // 默认显示main路由
            path: '/',
            redirect: '/main'
        },
        {
            path: '/main',
            component: Main
        }, {
            path: '/about',
            component: About
        }, {
            path: '/university',

            component: University
        }, {
            path: '/result',
            name: 'searchRes',
            component: Result
        }, {
            path: '/SubjectRanking',
            name: 'SubjectRanking',
            component: SubjectRanking
        }, {
            path: '/SoftRanking',
            name: 'SoftRanking',
            component: SoftRanking
        }, {
            path: '/MajorRanking',
            name: 'MajorRanking',
            component: MajorRanking
        }
    ]
})
router.beforeEach((to, from, next) => {
    // 路由守卫

    if (to.path === '/main' || to.path === '/About') {
        next()
    } else if (sessionStorage.getItem('token')) {
        next()
    } else {
        next(false);
        alert('请先登录')
    }
})
export default router