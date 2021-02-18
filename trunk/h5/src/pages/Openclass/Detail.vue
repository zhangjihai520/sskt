<template>
  <div class="page">
    <div class="container">
      <div class="wrapper">
        <div class="title">{{title}}</div>
        <section class="videoWrapper">
          <div id="mse" style="backgroundColor: 'rgba(0,0,0,0.87)'"></div>
        </section>
        <ul class="courseList" v-if="list.length > 0">
          <li
            class="item"
            v-for="item in list"
            :key="item.subjectId"
            :class="{
              'now': item.StatusFlag === ENUM.InClass,
              'enable': item.StatusFlag <= ENUM.WaittingForClass,
              'do': item.StatusFlag === ENUM.ClosedClass && item.FileDownUrl,
              'init': item.IsZzzy === true
            }">
            <div class="timeAndCourse">
              <p class="time">{{item.BeginTime}}</p>
              <p class="course">{{item.CourseName}}</p>
            </div>
            <div class="knowladge">{{item.SubjectName}}</div>
            <button
              class="button"
              v-if="item.IsZzzy === true"
            >无直播</button>
            <button
              class="button"
              v-else-if="item.FileDownUrl && item.StatusFlag===5"
              @click="onRedirectToUrl(item.FileDownUrl)"
            >立刻练习</button>
            <template v-else>
              <button class="button"
                @click="onPlayThis(item.SubjectId)"
                v-if="item.StatusFlag === ENUM.ClosedClass">点击回看</button>
              <button class="button"
                @click="onPlayThis(item.SubjectId)"
                v-if="item.StatusFlag === ENUM.InClass">正在直播</button>
              <button class="button" v-if="item.StatusFlag <= ENUM.WaittingForClass">暂未开始</button>
            </template>
          </li>
        </ul>
      </div>
      <div class="downloadBtn" @click="onRedirectToUrl('http://app.motk.com/android.html?c=A01')">点击完成课后作业</div>
    </div>
  </div>
</template>

<script>
import { GetPublicBenefitSubjectList, CurrentTime, RecordLog } from "../../services/getData";
import Player from 'xgplayer'
// import 'xgplayer-mp4';

export default {
  name: 'OpenClassDetail',
  inject: ['reload'],
  data() {
    return {
      subjectId: '',
      gradeId: '',
      date: '',
      list: [],
      title: '',
      ENUM: {
        WaittingForClass: 4, // 未开始
        ClosedClass: 5, // 已结束
        InClass: 6 // 直播中
      },
      current: null,
      currentTime: 0, // 当前播放时间差
      player: null,
      config: null,
    }
  },
  mounted() {
    const { subjectId, gradeId, date } = this.$route.query;
    const UserRole = localStorage.getItem('UserRole') || 1;
    this.date = date;
    this.subjectId = encodeURIComponent(subjectId).toLocaleLowerCase();
    this.gradeId = +gradeId;
    this.UserRole = UserRole;

    this.recordLog();
    this.getLessonData();
  },
  methods: {
    initPlayer () {
      let self = this;
      if (this.config.url && this.config.url !== '') {
        this.player = new Player(this.config);
        if (this.config.autoplay && this.currentTime > 0) {
          this.player.video.currentTime = 100
          this.player.play()
          this.player.pause()
        }
      }
    },
    async recordLog() {
      const { subjectId } = this.$route.query;
      RecordLog({
        ObjectKey: subjectId,
        RecordType: 0,
      });
    },
    async getLessonData() {
      const { UserRole, gradeId, date, subjectId } = this;
      const list = [];
      const res = await GetPublicBenefitSubjectList({
        Date: date, //日期
        GradeId: gradeId, //年级枚举值
        CourseId:0, //所属学科枚举值
        UserRole: UserRole //1学生，2老师，3管理员，4家长
      });
      if (res.DateGroup && res.DateGroup.length > 0) {
        res.DateGroup.forEach(item => {
          list.push(...item.GroupClassInfoList)
        });
      }
      let current = list.filter(item => {
        return item.SubjectId.toLocaleLowerCase() === subjectId
      })[0];
      current = current ? current : list[0];

      // 设置标题
      document.title = current.CourseName;
      this.title = current.SubjectName || '中高考复习课';
      this.list = list;
      // 设置视频播放
      this.videoConfigSetting (current);
    },

    // 获取当前直播的时间
    async getCurrentTime(endTime, callback) {
      const res = await CurrentTime({
        InputDate: endTime
      });
      callback && callback(res.SpaceOfTime);
    },

    onPlayThis(subjectId) {
      const gradeId = this.gradeId
      const date = this.date
      if (this.subjectId === subjectId) return false;
      const url = `/OpenClass/Detail?subjectId=${subjectId}&date=${date}&gradeId=${gradeId}`;
      this.subjectId = subjectId;
      this.$router.replace(url);
      this.reload();
    },
    /**
     * 设置播放器的配置
     * @param {Object} current 当前视频的信息
     */
    videoConfigSetting (current) {
      const configuration = {
        id:'mse',
        url: `${current.PPTVideoUrl}?t=${Math.random()}`,
        poster: current.ImagePath, // 视频封面
        fluid: true,
        fitVideoSize: 'auto',
        autoplay: false,
        loop: false,
        videoInit: true,
        playsinline: true,
        'x5-video-orientation': 'portraint',
        rotate: {   //视频旋转按钮配置项
            innerRotate: true, //只旋转内部video
            clockwise: false // 旋转方向是否为顺时针
        }
      }

      this.getCurrentTime(current.EndTime, (time) => {
        if (current.StatusFlag === this.ENUM.InClass && time > 0) {
          this.currentTime = time;
          configuration.ignores = ['time', 'progress', 'replay']
          configuration.autoplay = true;
        }
        this.config = configuration;
        this.initPlayer();

      });
    },
    beforeUpdate() {
      this.initPlayer();
    },
    beforeDestroy() {
      this.player && typeof this.player.destroy === 'function' && this.player.destroy();
    },
    onRedirectToUrl(url) {
      window.location.href = url;
    }
  }
}
</script>

<style lang="less" scoped>
@import "~vux/src/styles/reset.less";
.page {
  // height: 100vh;
  width: 100%;
  overflow-x: hidden;
  background: url('../../../static/images/openclass/bg.jpg') no-repeat;
  background-size: 100% 100%;
  overflow-y: auto;
}
.container {
  padding: 16px 22px;
}
.wrapper {
  background: #fff;
  border-radius: 5px;
  padding-bottom: 10px;
  .title {
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
    margin: 0 22px;
    min-height: 187px;
    border-radius: 5px;
    overflow: hidden;
  }
}
#mse {
  width: 100% !important;
  height:100% !important;
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

.downloadBtn {
  color: #fff;
  text-align: center;
  height: 80px;
  line-height: 80px;
  margin: 20px 80px 0;
  border: solid 1px #fff;
  border-radius: 60px;
}
</style>
