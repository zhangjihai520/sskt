import CourseManager from '../../pages/Student/CourseManager';
import MyCourse from '../../pages/Student/CourseManager/MyCourse';
import ReservationCourse from '../../pages/Student/CourseManager/ReservationCourse';
import LiveStream from '../../pages/Student/LiveStream';
import RecordStream from '../../pages/Student/RecordStream';
import MinLesson from '../../pages/Student/MinLesson';
import MinLessonVideo from '../../pages/Student/MinLessonVideo';
import Message from '../../pages/Student/Message';
import ClassroomExercises from '../../pages/Student/ClassroomExercises';
import ClassPractice from '../../pages/Student/ClassPractice';
import TimetableManagement from '../../pages/Student/TimetableManagement';
import PersonalCenter from '../../pages/Student/PersonalCenter';
import ViewAssessment from '../../pages/Student/ViewAssessment';
import PublicClass from '../../pages/Student/PublicClass';
import PublicClassLiveStream from '../../pages/Student/PublicClass/LiveStream';
import MyEvaluation from '../../pages/Student/EvaluationManagement/MyEvaluation';
import EvaluationToMe from '../../pages/Student/EvaluationManagement/EvaluationToMe';
/**
 * 学生路由配置
 */
const commonData = [
  {
    name: '课表管理',
    path: 'ScheduleManager',
    component: TimetableManagement
  },
  {
    name: '个人中心',
    path: '',
    component: PersonalCenter
  },
  {
    name: '消息中心',
    path: 'Message',
    component: Message
  },
  {
    name: '我的课程',
    path: 'MyCourse',
    component: MyCourse
  },
  {
    name: '预约课程',
    path: 'ReservationCourse',
    component: ReservationCourse
  },
  {
    name: '课程管理',
    path: 'CourseManager',
    children: [
      {
        name: '课程直播',
        path: 'liveStream',
        component: LiveStream
      },
      {
        name: '课程录播',
        path: 'recordStream',
        component: RecordStream
      },
      {
        name: '课前测试',
        path: 'ClassroomExercises',
        component: ClassroomExercises
      },
      {
        name: '随堂练习',
        path: 'ClassPractice',
        component: ClassPractice
      },
      {
        name: '查看测评',
        path: 'ViewAssessment',
        component: ViewAssessment
      }
    ]
  },
  {
    name: '微课',
    path: 'MinLesson',
    component: MinLesson,
    children: [
      {
        name: '微课视频',
        path: 'MinLessonVideo',
        component: MinLessonVideo
      }
    ]
  },
  {
    name: '我的评价',
    path: 'MyEvaluation',
    component: MyEvaluation
  },
  {
    name: '对我的评价',
    path: 'EvaluationToMe',
    component: EvaluationToMe
  },
  {
    name: '中高考复习课',
    path: 'AntiVirusPublicClass',
    component: PublicClass,
    children: [
      {
        name: '课程直播',
        path: 'PublicClassLiveStream',
        component: PublicClassLiveStream
      }
    ]
  }
];

/**
 * 线上
 */
export const studentOnlineNav = {
  name: '学生',
  path: '/StudentOnline',
  children: commonData
};

/**
 * 线下
 */
export const studentOfflineNav = {
  name: '学生',
  path: '/StudentOffline',
  children: commonData
};
