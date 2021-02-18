<template>
  <div class="page">
    <div class="switchUrl" v-show="buttonShow"  @click="handleSwitch()">
      <img alt="切换画面" src="/static/images/common/switch.png">
    </div>
    <div class="pay">
      <div class="wrapper">
        <section class="videoWrapper">
          <div id="mse" style="backgroundColor: 'rgba(0,0,0,0.87)'"></div>
        </section>
      </div>
    </div>
    <wxChat
      :data="wxChatData"
      @changeUpperId = "changeUpperId"
      :getUpperData="getUpperData"
      :ownerUserId="UserId"
      :width="width"
      :isIos="isIos"
      :isShow="true"
    ></wxChat>
  </div>
</template>

<script>
import {
  GetSubjectVideo,
  GetMessage,
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
  name: "TeacherRecordStream",
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
      UserId: 0,
      HelpTeacherId: 0,
      RoomUserType: 0,
      buttonShow: false,
      subjectId: "",
      title: "",
      videoData: {},
      videoList: [],
      urlIndex: 0,
      player: null,
      config: null,
      userInfoList: [],
      colorData: colorArr,
      wxChatData: [],
      isAndroid: false,
      isIos: false,
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
      }
      if (result.UserInfoList && result.UserInfoList.length > 0) 
      {
        result.UserInfoList.forEach(item => {
          if(item.RoomUserType == 1)
          {
            this.HelpTeacherId = item.UserId;
          }
        });
        this.userInfoList = result.UserInfoList;
      }
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
     
      (this.UserId = current.Uid), this.getMessage();
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
        "x5-video-player-type": "h5",
        "x5-video-player-fullscreen": false,
        "x5-video-orientation": "landscape",
        closeDefaultBtn: false, //开启此项后不使用默认提供的弹幕开关，默认使用西瓜播放器提供的开关
        defaultOff: false //开启此项后弹幕不会初始化，默认初始化弹幕
      };
      this.config = configuration;
      this.initPlayer();
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
  .switchUrl{
      position: absolute;
      color: #fff;
      z-index: 999;
      top: 10px;
      right: 10px;
  }
}
.weui-tab__panel {
  padding-bottom: 0px;
}
.container {
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
  width: 100% !important;
  height: 100% !important;
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
    .helpTeacherMsg{
      display: inline-block;
      height: 60px;
      width: 68%;
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
