import Vue from 'vue'
import Router from 'vue-router'
import index from '@/components/index'
import videoPlay from '@/components/videoPlay'
import videoList from '@/components/videoList'
import imgList from '@/components/imgList'
import systemConfig from '@/components/systemConfig'
// import videoUpload from '@/components/videoUpload'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
    {
      path: '/videoPlay',
      name: 'videoPlay',
      component: videoPlay
    },
    {
      path: '/videoList',
      name: 'videoList',
      component: videoList,
      meta: {
		requireAuth: true,
		keepAlive: false, //此组件不需要被缓存
      },
    },
    {
      path: '/imgList',
      name: 'imgList',
      component: imgList
    },
    {
      path: '/system/config',
      name: 'systemConfig',
      component: systemConfig
    },
    
    
    
//  {
//    path: '/videoUpload',
//    name: 'videoUpload',
//    component: videoUpload
//  }
  ]
})
