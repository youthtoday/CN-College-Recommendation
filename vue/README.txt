项目运行 首先打开命令行安装依赖 npm install
安装完毕后 npm run dev 即可运行在本地服务器中

.git git管理文件
images 校徽图
imagesbk 风景图
node_modules 包管理文件
public 公共静态资源
index.html 项目首页
src 项目源代码文件
src-- >
  assets 静态图片
  App.vue 根组件
  main.js 总管理文件
  router.js 路由管理文件
  components.js 所有组件，以模块方式导出
  eventBus.js 负责兄弟组件通信的js文件
  js js文件
  js-- >
    animate.js 动画函数
    myAxios.js axios全局配置模块
    selectItem 下拉框选项模块
  components 组件管理文件
  components -->
    About.vue 关于我们页面
    Header.vue 头部导航
    login.vue 登录模块
    register.vue 注册模块
    Main.vue 首页
    test.vue 测试页面
    result.vue 大学详情页面
    University.vue 院校页面
    components -->ranking
        majorRanking.vue 专业详情页
        softRanking.vue 大学排名页
        subjectRanking.vue 专业排名页面