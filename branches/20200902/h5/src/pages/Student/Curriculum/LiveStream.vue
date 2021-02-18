<template>
  <div class="page">
    <div class="pay">
      <div class="wrapper">
        <div class="title">{{title}}</div>
        <div class="tool">
          <x-icon
            type="ios-reverse-camera-outline"
            size="30"
            v-show="buttonShow"
            @click="handleSwitch()"
          >切换画面</x-icon>
        </div>
        <section class="videoWrapper">
          <div id="mse" style="backgroundColor: 'rgba(0,0,0,0.87)'"></div>
        </section>
      </div>
    </div>
    <!-- <panel class="list" :list="list"></panel> -->
    <wxChat
      :data="wxChatData"
      @changeUpperId = "changeUpperId"
      :getUpperData="getUpperData"
      :ownerUserId="UserId"
      :width="width"
      :isIos="isIos"
    ></wxChat>
    <div class="send" gutter="0">
      <!--<div class="tool">
        <x-icon type="happy-outline" size="28" class="icon"></x-icon>
        <x-icon type="android-camera" size="28" class="icon"></x-icon>
        <x-icon type="android-folder" size="28" class="icon"></x-icon>
      </div>-->
      <div v-if="UserStatus == 0" class="txt">
        <div class="xw-chat-tool-item">
          <transition name="cb_helpTeacher">
            <input class="xw-checkbox" type="checkbox" @click="cbClick($event)">
          </transition>
        </div>
        <x-textarea 
          class="msg"
          :disabled="(UserStatus > 0)"
          :placeholder="(cbCheck ? '@助教老师':'请输入内容')"
          :max="100"
          :show-counter="false"
          v-model="content"
          :height="30"
          @click="scrollToBottom"
          @focus="scrollToBottom"
          @blur="scrollToBottom"
        ></x-textarea>
        <div class="xw-chat-tool-item">
          <transition name="facai">
            <x-icon type="happy-outline" size="28" class="icon xw-chat-tool-btn" @click="emojiFuc" ></x-icon>
          </transition>
        </div>
        <div class="xw-chat-tool-item send">
          <transition name="send">
            <a
              href="javascript:void(0)"
              class="xw-send-btn-text"
              v-if="content.trim().length && UserStatus==0"
              @click="sendMessage"
            >发送</a>
            <photograph @upload="upload"></photograph>
          </transition>
          
        </div>
      </div>
      <div v-if="UserStatus > 0" class="noTalk">
        禁言中
      </div>
      <div class="emotion-list"  v-if="showEmoji && UserStatus == 0">
        <img
          v-for="(item, index) in emojiData"
          :key="index"
          class="static-emotion-gif"
          style="vertical-align: middle"
          :src="`https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/${index}.gif`"
          :data="`[${item}]`" v-on:click="content+=`[${item}]`"
        />
      </div>
    </div>
  </div>
</template>

<script>
import {
  GetSubjectVideo,
  GetMessage,
  SendMessage,
  SendFile,
  RecordLog
} from "../../../services/getData";
import wxChat from "../../../components/wxChat.vue";
import photograph from "../../../components/photograph.vue";
import { emojiArr,colorArr } from "../../../config/const";
import Player from "xgplayer";
import FlvJsPlayer from "xgplayer-flv.js";
import HlsJsPlayer from "xgplayer-hls.js";
// import 'xgplayer-mp4';
import {
  Group,
  XTextarea,
  Toast,
  Panel,
  Flexbox,
  FlexboxItem,
  XButton
} from "vux";

export default {
  name: "StudentLiveStream",
  components: {
    wxChat,
    XTextarea,
    Group,
    Toast,
    Panel,
    Flexbox,
    FlexboxItem,
    photograph
  },
  data() {
    return {
      upperId: 0,
      width: window.screen.width,
      showEmoji: false,
      timeoutObj: null,
      UserId: 0,
      HelpTeacherId: 0,
      RoomUserType: 0,
      lockReconnect: false,
      noTalkingChecked: false,
      wsUrl: "",
      ws: null,
      buttonShow: false,
      subjectId: "",
      title: "",
      videoData: {},
      videoList: [],
      urlIndex: 0,
      player: null,
      config: null,
      content: "",
      playTimeZone: 0,
      playTimeFlag: 0,
      userInfoList: [],
      UserStatus: 0,
      emojiData: emojiArr,
      colorData: colorArr,
      wxChatData: [],
      isAndroid: false,
      isIos: false,
      waitTimeZone: 0,
      devTimeZone: 0,
      playerReady: false,
      cbCheck: false
    };
  },
  created() {
    this.initWidth();
  },
  mounted() {
    const { subjectId } = this.$route.query;
    this.subjectId = encodeURIComponent(subjectId);
    this.recordLog();
    this.getLessonData();
    this.resetHeight();
  },
  destroyed: function() {
    this.ws.close();
    clearInterval(this.timeoutObj);
    this.player.destroy();
    this.player = null;
  },
  methods: {
    resetHeight(){
      document.getElementById('vux_view_box_body').style.paddingBottom="0px";
      document.getElementById('app').style.height="100%";
      document.getElementsByClassName('main')[0].style.height="100%";
      document.getElementsByClassName('content')[0].style.height="100%";
    },
    //点击控制表情切换（显示和隐藏）
    emojiFuc() {
      if(this.UserStatus>0)
      {
        return;
      }
      this.showEmoji = !this.showEmoji;
    },
    initWidth() {
      var ua = navigator.userAgent;
      var ipad = ua.match(/(iPad).*OS\s([\d_]+)/),
        isIphone = !ipad && ua.match(/(iPhone\sOS)\s([\d_]+)/),
        isAndroid = ua.match(/(Android)\s+([\d.]+)/),
        isMobile = isIphone || isAndroid;
      //非移动端设置400px宽度，移动端是100%
      if (!isMobile) {
        this.width = 400;
      }else{
        if(isAndroid)
        {
          this.isAndroid = true;
        }
        if(ipad||isIphone)
        {
          this.isIos = true;
        }
      }
    },
    upload(file){
      if (!file) {
        return;
      }
      const imgMasSize = 1024 * 1024 * 3;
      // check extension
      if (['jpeg', 'png', 'jpg'].indexOf(file.type.split('/')[1]) < 0) {
       // message.destroy();
      //  message.error('图片格式支持JPG、JPEG、PNG格式');
        return;
      }

      // check file size
      if (file.size > imgMasSize) {
       // message.destroy();
        //message.error('图片大小不超过3M');
        return;
      }

      const formData = new FormData();
      formData.append('SubjectId', this.subjectId);
      formData.append('FileType', 2);
      formData.append('file', file);
      formData.append('ToUserId', this.cbCheck ? this.HelpTeacherId : 0);
      const result = SendFile(formData);
      this.showEmoji = false;
    },

    getColor(){
      return this.colorData[Math.floor(Math.random()*this.colorData.length)]; 
    },
    //向上滚动加载数据
    getUpperData() {
      var me = this;
      return GetMessage({
          SubjectId: this.subjectId,
          TypeId: 1,
          BoundaryId: me.upperId,
          PageSize: 20,
          ToUserId: -1
        });
    },
    changeUpperId(changeUpperId){
      this.upperId = changeUpperId;
    },
    initPlayer() {
      var self = this;
      if (self.config.url && self.config.url !== "") {
        if (self.config.url.indexOf(".mp4") > -1) {
          self.player = new Player(self.config);
        } else if (self.config.url.indexOf(".m3u8") > -1) {
          self.player = new HlsJsPlayer(self.config);
        } else {
          self.player = new FlvJsPlayer(self.config);
        }
        self.player.on('pause', function() {
          if(self.playTimeFlag > 0)
          {
            self.player.danmu.bulletBtn.main.data=[];
            self.playTimeZone = new Date().getTime();
            self.playTimeFlag = 0;
           // self.player.danmu.bulletBtn.main.dataHandleTimer = 0;
          }
          self.player.play();
        });
        self.player.once('play', function(pl) {
          self.waitTimeZone = 0;
        });
        self.player.once('ready',function(pl){
            self.playerReady = true;
        });
        self.player.on('playing', function(pl) {
          if(self.playTimeZone == 0)
          {
            self.player.danmu.bulletBtn.main.data=[];
            self.playTimeZone = new Date().getTime();
          }
          if(self.waitTimeZone > 0)
          {
            var dt = new Date().getTime() - self.waitTimeZone;
            self.devTimeZone += dt;
            self.waitTimeZone = 0;
          }          
        });
        self.player.on('exitFullscreen',function() {
          self.playTimeFlag = 1;
          self.waitTimeZone = 0;
          self.devTimeZone = 0;
          self.player.reload();
        });
        self.player.on('waiting', function() {
          self.waitTimeZone = new Date().getTime();
          self.player.danmu.bulletBtn.main.data = [];
        });
        if (self.config.autoplay) {
          self.player.reload();
        }
      }
    },
    async getMessage() {
      const result = await GetMessage({
        SubjectId: this.subjectId,
        TypeId: 3,
        PageSize: 20,
        ToUserId: -1,
      });
      if (result.TalkMessageList && result.TalkMessageList.length > 0) {
        this.wxChatData = result.TalkMessageList;
        this.upperId = result.TalkMessageList[0].TalkMessageId;
        this.UserStatus=result.RoomStatus;
      }
      if (result.UserInfoList && result.UserInfoList.length > 0) 
      {
        result.UserInfoList.forEach(item => {
          if(item.UserId == this.UserId)
          {
            this.UserStatus=item.Status;
          }
          if(item.RoomUserType == 1)
          {
            this.HelpTeacherId = item.UserId;
          }
        });
        this.userInfoList = result.UserInfoList;
      }
    },

    sendMessage() {
      const result = SendMessage({
        SubjectId: this.subjectId,
        MessageContent: this.content,
        ToUserId: this.cbCheck ? this.HelpTeacherId : 0
      });
      this.content = "";
      this.showEmoji=false;
    },
    async recordLog() {
      RecordLog({
        ObjectKey: this.subjectId,
        RecordType: 0
      });
    },

    async getLessonData() {
      const current = await GetSubjectVideo({
        SubjectId: this.subjectId
      });

      // 设置标题
      document.title = current.SubjectName;
      this.title = current.SubjectName;
      let result = [];
      if (current.VideoUrl) {
        current.VideoUrl.map(item => {
          result.push(item);
        });
      }
      if (current.PPTVideoUrl) {
        current.PPTVideoUrl.map(item => {
          result.push(item);
        });
      }
      this.videoList = result;
      if (this.videoList.length > 1) {
        this.buttonShow = true;
      }
      // 设置视频播放
      this.videoConfigSetting(result[0]);
      let protocol = location.protocol === "https:" ? "wss:" : "ws:";
      let url =
        protocol +
        "//sskt.nceduc.cn/api/chat/" +
        //"//192.168.50.17:8080/api/chat/" +
        current.Channel +
        "/" +
        current.Uid +
        "_H5";
      //let url = protocol+"://sskt.nceduc.cn/api/chat/"+current.Channel+"/"+current.Uid; //192.168.50.17:8080/api/chat/
      (this.UserId = current.Uid), (this.wsUrl = url), this.getMessage();this.initWebSocket();
    },

    handleSwitch() {
      if (this.urlIndex == this.videoList.length - 1) {
        this.videoConfigSetting(this.videoList[0]);
        this.urlIndex = 0;
      } else {
        this.urlIndex = this.urlIndex + 1;
        this.videoConfigSetting(this.videoList[this.urlIndex]);
      }
    },
    appSource() {
      const u = navigator.userAgent.toLowerCase;
      const isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
      if (isiOS) {
        return "ios";
      } else {
        return "andriod";
      }
    },
    /**
     * 设置播放器的配置
     * @param {Object} current 当前视频的信息
     */
    videoConfigSetting(url) {
      if(this.player)
      {
        this.player.destroy();
        this.player = null;
      }
      const configuration = {
        id: "mse",
        url: url,
        lang: "zh-cn",
        fluid: true,
        fitVideoSize: "auto",
        volume: 1,
        autoplay: true,
        loop: false,
        videoInit: true,
        playsinline: this.isIos,
        closeVideoClick: true,
        closeVideoTouch: true,
        closeVideoDblclick: true,
        ignores : ['play', 'time', 'progress', 'replay'],
        danmu: {
          comments: [],
          area: {
            //弹幕显示区域
            start: 0, //区域顶部到播放器顶部所占播放器高度的比例
            end: 1 //区域底部到播放器顶部所占播放器高度的比例
          }
        },
        "x5-video-player-type": "h5",
        "x5-video-player-fullscreen": false,
        "x5-video-orientation": "landscape",
        closeDefaultBtn: false, //开启此项后不使用默认提供的弹幕开关，默认使用西瓜播放器提供的开关
        defaultOff: false //开启此项后弹幕不会初始化，默认初始化弹幕
      };
      
      this.config = configuration;
      this.initPlayer();
    },
    async initWebSocket() {
      this.ws = new WebSocket(this.wsUrl);
      if (typeof this.ws == "undefined") {
        console.log("您的浏览器不支持WebSocket");
      } else {
        console.log("您的浏览器支持WebSocket");
        this.ws.onmessage = this.websocketonmessage;
        this.ws.onopen = this.websocketonopen;
        this.ws.onerror = this.websocketonerror;
        this.ws.onclose = this.websocketclose;
        window.onbeforeunload = event => {
          console.log("关闭WebSocket连接！");
          this.ws.close();
        };
      }
    },
    websocketonopen() {
      //连接建立之后执行send方法发送数据
      console.log(this.wsUrl + " Connection open ...");
      this.reset();
      this.start();
    },
    websocketonerror() {
      //连接建立失败重连
      this.lockReconnect = false;
    },
    websocketonmessage(evt) {
      const self = this;
      //数据接收
      self.lockReconnect = true;
      //console.log( "Received Message: " + evt.data);
      if (evt.data == "HeartBeat") {
        console.log("HeartBeat");
        return;
      }
      let obj = JSON.parse(evt.data);
      if (obj.TalkMessage) {
        if (obj.MessageType == 0) {
          if (obj.TalkMessage.TalkTypeId == 1) {
            obj.TalkMessage.Content = obj.TalkMessage.Content.replace(
              /(\r\n|\n|\r)/gm,
              "<br />"
            );
            if(obj.TalkMessage.ToUserId == 0 && this.playerReady){
              var startTime = new Date().getTime() - this.playTimeZone - this.devTimeZone;
              //this.title = new Date().getTime()+":"+this.playTimeZone+":"+startTime+":"+this.devTimeZone;
              this.player.danmu.sendComment({
                duration: 3000, //弹幕持续显示时间,毫秒(最低为5000毫秒)
                id: obj.TalkMessage.TalkMessageId + "", //弹幕id，需唯一
                start: startTime, //弹幕出现时间，毫秒
                //prior: true, //该条弹幕优先显示，默认false
                color: true, //该条弹幕为彩色弹幕，默认false
                txt: obj.TalkMessage.UserTrueName+":"+obj.TalkMessage.Content,//+":"+this.playTimeZone+":"+new Date().getTime()+":"+startTime+":"+ this.devTimeZone, //弹幕文字内容
                style: {
                  color: self.getColor(),
                  fontSize: "16px",
                  padding: "3px 6px",
                }
              });
            }
          }
          //this.list = [...this.list,obj.TalkMessage];
          if(obj.TalkMessage.ToUserId == 0 || (obj.TalkMessage.ToUserId > 0 && (obj.TalkMessage.ToUserId == this.UserId || obj.TalkMessage.UserId == this.UserId))){
            let socketData = [];
            socketData.push(obj.TalkMessage);
            this.wxChatData = socketData ;
          }
          
        }
      }
      if (obj.UserInfoList) {
        obj.UserInfoList.forEach(item => {
          if(item.UserId == this.UserId)
          {
            this.UserStatus = item.Status;
            if(this.UserStatus>0)
            {
              this.showEmoji = false;
            }
          }
        });
        this.userInfoList = obj.UserInfoList;
      }
      //this.handleToNewMessage();
    },
    websocketsend(Data) {
      //数据发送
      this.ws.send(Data);
    },
    websocketclose(e) {
      //关闭
      console.log("Connection closed.");
      this.lockReconnect = false;
    },
    reset() {
      clearInterval(this.timeoutObj);
      return this;
    },
    start() {
      const self = this;
      this.timeoutObj = setInterval(function() {
        //这里发送一个心跳，后端收到后，返回一个心跳消息，
        //onmessage拿到返回的心跳就说明连接正常
        if (self.ws.readyState === 1) {
          self.ws.send("HeartBeat");
        }
        if (!self.lockReconnect) {
          self.reconnect();
        }
      }, 60000);
    },
    reconnect() {
      const self = this;
      if (this.lockReconnect) return;
      this.lockReconnect = true;
      //没连接上会一直重连，设置延迟避免请求过多
      setTimeout(function() {
        if (self.ws.readyState === 2 || self.ws.readyState === 3) {
          self.reset();
          self.initWebSocket();
          self.lockReconnect = false;
        }
      }, 2000);
    },
    beforeUpdate() {
      this.initPlayer();
    },
    beforeDestroy() {
      this.player &&
        typeof this.player.destroy === "function" &&
        this.player.destroy();
    },
    onRedirectToUrl(url) {
      window.location.href = url;
    },
    cbClick(e) {
        this.cbCheck = e.target.checked;
    },
    scrollToBottom: function () {
      this.$nextTick(() => {
        var scrollContainer = document.getElementById('scrollLoader-container');
        scrollContainer.scrollTop = scrollContainer.scrollHeight;
        var wxchatScrollContainer = document.getElementById('wxchat-container');
        wxchatScrollContainer.scrollTop = wxchatScrollContainer.scrollHeight;
      });
    }

  }
};
</script>
<style lang="less" scoped>
@import "~vux/src/styles/reset.less";
.page {
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  overflow: hidden;
}
.weui-tab__panel {
  padding-bottom: 0px;
}
.wrapper {
  background: #fff;
  border-radius: 5px;
  .tool {
    position: absolute;
    right: 25px;
    display: inline-block;
    padding: 10px 0;
    font-size: 32px;
  }
  .title {
    display: inline-block;
    height: 80px;
    line-height: 80px;
    padding: 0 22px;
    font-size: 32px;
    color: #333;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }
  .videoWrapper {
    min-height: 187px;
    border-radius: 5px;
    overflow: hidden;
  }
}
#mse {
  width: 100%;
  height: 100%;
}

.courseList {
  padding: 4px 20px;

  .item {
    display: flex;
    align-items: center;
    margin-top: 22px;

    .timeAndCourse {
      color: #666;
      font-size: 28px;
      margin-right: 20px;
      width: 120px;

      p {
        margin: 0;
      }
    }
    .knowladge {
      flex: 1;
      display: -webkit-box;
      color: #666;
      font-size: 28px;
      text-overflow: ellipsis;
      overflow: hidden;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      max-height: 44px;
    }
    .button {
      width: 166px;
      height: 60px;
      text-align: center;
      line-height: 60px;
      font-size: 24px;
      border: solid 1px #4173f2;
      color: #4173f2;
      background: none;
      border-radius: 30px;
      margin-left: 40px;
    }
  }

  .now {
    .timeAndCourse {
      color: #fc670b;
    }
    .knowladge {
      color: #fc670b;
    }
    .button {
      color: #fc670b;
      border-color: #fc670b;
    }
  }

  .enable {
    .timeAndCourse {
      color: #666;
    }
    .knowladge {
      color: #999;
    }
    .button {
      color: #999;
      border-color: #999;
    }
  }

  .do {
    .button {
      border-color: #333;
      color: #333;
    }
  }

  .init {
    .timeAndCourse {
      color: #333;
    }
    .knowladge {
      color: #333;
    }
    .button {
      color: #333;
      border-color: #333;
    }
  }
}

.xw-chat-tool-btn {
  line-height: 1.41176471;
  display: block;
  margin-top: 25%;
  height: 58px;
  width: 58px;
  border-radius: 100%;
  text-indent: -9999px;
  opacity: 0.5;
  overflow: hidden;
  transform: translateZ(0);
  margin-left: 10px;
  fill: #3f87fe;
}

.pay {
  flex-grow: 0;
  flex-shrink: 0;
}
.send {
  flex-grow: 0;
  flex-shrink: 0;
  // height: 100px;
  .tool {
    padding: 10px;
    .icon {
      padding: 0 10px;
      fill: #3f87fe;
    }
  }
  .noTalk{
    text-align: center;
    margin-top: 20px;
    height: 70px;
    width: 100%;
    color: rgb(201, 197, 197);
  }
  .txt {
    display: flex;
    .msg {
      display: inline-block;
      height: 60px;
      width: 60%;
      padding: 20px 40px;
    }
    .xw-chat-tool-item {
      display: inline-block;
      font-size: 32px;
      height: 60px;
      .xw-send-btn-text {
        position: relative;
        top: 45%;
        height: 60px;
        padding-left: 15px;
        color: #09bb07;
      }
      .xw-checkbox{
        position: relative;
        zoom: 130%;
        top: 32%;
        height: 60px;
        margin-left: 15px;
      }
    }
  }
  .emotion-list {
    margin: 0 auto;
    height: 300px;
    overflow-y: auto;
    img {
      padding: 15px  15px;
    }
  }
}
h1,
h2 {
  font-weight: normal;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
}
.list {
  margin-top: 0;
  flex-grow: 1;
  flex-shrink: 1;
  overflow-y: auto;
  background-color: #fff;
  .header {
    padding: 14px 15px 10px;
    color: #505050;
    font-size: 30px;
  }
  .item {
    margin: 15px 20px;
    border-top: 1px solid #e5e5e5;
    h4 {
      padding: 10px 0;
      font-size: 14px;
      color: #999;
    }
    p {
      font-size: 12px;
      color: #505050;
      word-break: break-all;
    }
  }
}
</style>
