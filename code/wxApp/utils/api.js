
import __config from '../config/env'

const request = (url, method, data, showLoading) => {
  let _url = __config.basePath + url
  return new Promise((resolve, reject) => {
    if (showLoading){
      wx.showLoading({
        title: '加载中',
      })
    }
    wx.request({
      url: _url,
      method: method,
      data: data,
      header: {
        'Authorization': getApp().globalData.thirdSession != null ? getApp().globalData.thirdSession : ''
      },
      success(res) {
        if (res.statusCode == 200) {
          if (res.data.code != 200) {
            console.log(res.data)
            wx.showModal({
              title: '提示',
              content: res.data.message ? res.data.message : '没有数据' + '',
              success() {
                
              },
              complete(){
                if(res.data.code == 60001){
                  //session过期，则清除过期session，并重新加载当前页
                  getApp().globalData.thirdSession = null
                  wx.reLaunch({
                    url: getApp().getCurrentPageUrlWithArgs()
                  })
                }
              }
            })
            reject(res.data.msg)
          }
          resolve(res.data)
        } else if (res.statusCode == 404) {
          wx.showModal({
            title: '提示',
            content: '接口请求出错，请检查手机网络',
            success(res) {

            }
          })
          reject()
        }  else if (res.statusCode == 401) {
          console.log(1)
          //登录
          wx.login({
            success: function (res) {
              if (res.code) {
                api.login({
                  code: res.code
                })
                  .then(res => {
                    console.log(res)
                    wx.hideLoading()
                    let wxUser = res.data
                    that.globalData.thirdSession = wxUser.token
                    that.globalData.wxUser = wxUser.user
                    resolve("success")
                   
                  })
              }
            }
          })
          reject()
        }else {
          console.log(res)
          wx.showModal({
            title: '提示',
            content: res.errMsg + ':' + res.data.message + ':' + res.data.msg,
            success(res) {

            }
          })
          reject()
        }
      },
      fail(error) {
        console.log(error)
        wx.showModal({
          title: '提示',
          content: '接口请求出错：' + error.errMsg,
          success(res) {

          }
        })
        reject(error)
      },
      complete(res) {
        wx.hideLoading()
      }
    })
  })
}

module.exports = {
  request,
  login: (data) => {//小程序登录接口
    return request('/login', 'post', data, false)
  },
  wxUserGet: (data) => {//微信用户查询
    return request('/wxUser', 'get', null, false)
  },
  wxUserSave: (data) => {//同步微信用户信息
    return request('/wxUserUpdate', 'post', data, true)
  },
  goodsCategoryGet: (data) => {//化妆品分类查询
    return request('/tree' , 'get', data, true)
  },
  goodsPage: (data) => {//化妆品列表
    return request('/goodsPage', 'post', data, false)
  },
  goodsScorePage:(data) => {//化妆品列表
    return request('/goodsScorePage', 'post', data, false)
  },
  goodsGet: (goodsId) => {//化妆品查询
    return request('/goodsDetail/' + goodsId, 'get', null, false)
  },
  //评价获取
  userCommentPage:(data)=>{
    return request('/userCommentPage', 'post', data, true)
  },
  //查看单个化妆品的评价
  goodsCommentPage:(data)=>{
    return request('/goodsCommentPage', 'post', data, true)
  },
  //查看所有动态数据信息
  forumPage:(data)=>{
    return request('/forumPage', 'post', data, true)
  },
  //发布动态数据
  forum:(data)=>{
    return request('/forum', 'post', data, true)
  },
  forumGet: (id) => {//动态详情数据获取
    return request('/forum/' + id, 'get', null, true)
  },
  comment: (data) => {//评价
    return request('/comment', 'post', data, true)
  },
  honorGet: (data) => {//榜单列表获取
    return request('/honorGet', 'get', null, true)
  },
  honorGoods:(id) => {//榜单商品数据获取
    return request('/honorGoods/' + id, 'get', null, true)
  },
  goodsComment: (data) => {//评价
    return request('/goodsComment', 'post', data, true)
  },
  myForumGet:(id) => {//自己发布的内容数据获取
    return request('/myForumGet/', 'get', null, true)
  },
  myCommentGet:(id) => {//自己的评论数据获取
    return request('/myComment/', 'get', null, true)
  },
  delForum:(id) => {//删除发布的内容
    return request('/delForum/'+ id, 'get', null, true)
  },
  delComment:(id) => {//删除评论内容
    return request('/delComment/'+ id, 'get', null, true)
  },
  getAllGoods:(id) => {//获取所有商品
    return request('/getAllGoods/'+id, 'get', null, true)
  },
  checkProduct:(pid,id) => {//产品比较
    return request('/checkProduct/'+pid+'/'+id, 'get', null, true)
  }
}