
const app = getApp()
import __config from '../../config/env'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    //图片
    imgList: [],
    //上传的图片String
    imgUrls: [],
    textareaAValue: '',
    picker: ['1.0', '1.5', '2.0', '2.5', '3.0', '3.5', '4.0', '4.5', '5.0'],
    goodsId: 0,
    pickIndex: 0
  },
 
  PickerChange(e) {
    console.log(e);
    this.setData({
      pickIndex: e.detail.value
    })
  },
  send(e) {
    var that = this
    app.api.goodsComment({
      content: that.data.textareaAValue,
      picList: that.data.imgUrls,
      goodsId: that.data.goodsId,
      score: that.data.picker[that.data.pickIndex]
    }, (res) => {
      
    })
    setTimeout(function() {
      wx.navigateTo({
        url: '../../pages/goods/goods-detail/index?id='+that.data.goodsId,
      })
    }, 1500);
   
  },
  ChooseImage() {
    var that = this
    wx.chooseImage({
      count: 1, //默认9
      sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album'], //从相册选择
      success: (res) => {
        var tempFilePaths = res.tempFilePaths
        wx.uploadFile({
          url: __config.upload,
          filePath: tempFilePaths[0],
          name: 'head',
          header: {
            "content-type": "multipart/form-data",
            "token": wx.getStorageSync('token')
          },
          success: function (res) {
            console.log(res)
            let data = JSON.parse(res.data);
            let path = data.data
            that.setData({
              imgUrls: that.data.imgUrls.concat(path)
            })
          }
        })
        if (this.data.imgList.length != 0) {
          this.setData({
            imgList: this.data.imgList.concat(res.tempFilePaths)
          })
        } else {
          this.setData({
            imgList: res.tempFilePaths
          })
        }
      }
    });
  },
  ViewImage(e) {
    wx.previewImage({
      urls: this.data.imgList,
      current: e.currentTarget.dataset.url
    });
  },
  DelImg(e) {
    wx.showModal({
      title: '警告',
      content: '确定删除吗',
      cancelText: '不确定',
      confirmText: '确定',
      success: res => {
        if (res.confirm) {
          this.data.imgList.splice(e.currentTarget.dataset.index, 1);
          this.setData({
            imgList: this.data.imgList
          })
        }
      }
    })
  },
  textareaAInput(e) {
    this.setData({
      textareaAValue: e.detail.value
    })
  },
  onLoad(options) {
    console.log(options)
    let that = this
    app.initPage()
      .then(res => {
        that.setData({
          goodsId: options.id
        })
      })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})