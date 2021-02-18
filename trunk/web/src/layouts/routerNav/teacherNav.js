import ClassScheduleList from '../../pages/Teacher/ClassSchedule/ClassScheduleList';
import CurriculumList from '../../pages/Teacher/Curriculum/CurriculumList/index';
import ClassroomWork from '../../pages/Teacher/Curriculum/ClassroomWork/ClassroomWork';
import SmallLessonList from '../../pages/Teacher/SmallLesson/SmallLessonList';
import SmallLessonEdit from '../../pages/Teacher/SmallLesson/SmallLessonEdit';
import ClassDetails from '../../pages/Teacher/Curriculum/Details/ClassDetails';
import ClassStudent from '../../pages/Teacher/Curriculum/Details/ClassStudent';
import EvaluateManagement from '../../pages/Teacher/TeachingEvaluation/EvaluateManagement';
import InfoManagement from '../../pages/Teacher/TeachingEvaluation/InfoManagement/index.js';
import EvaluateCourses from '../../pages/Teacher/TeachingEvaluation/EvaluateCourses/index.js';
import WorkStatistics from '../../pages/Teacher/Curriculum/ClassroomWork/WorkStatistics';
import Workanalysis from '../../pages/Teacher/Curriculum/ClassroomWork/Workanalysis';
import Workpreview from '../../pages/Teacher/Curriculum/ClassroomWork/Workpreview';
import Classdata from '../../pages/Teacher/Curriculum/Classdata';
import Situation from '../../pages/Teacher/Situation';
import Listenclass from '../../pages/Teacher/Curriculum/Listenclass';
import RecordStream from '../../pages/Teacher/ClassSchedule/RecordStream';
import PublicClass from '../../pages/Teacher/PublicClass';
import LiveStream from '../../pages/Teacher/Curriculum/LiveStream/index';
import PublicClassLiveStream from '../../pages/Teacher/PublicClass/LiveStream';

import ClassManagement from '../../pages/Teacher/ClassManagement';

/**
 * 教师路由配置
 */
const commonData = [
  {
    name: '课表管理',
    path: 'ClassSchedule',
    component: ClassScheduleList,
    children: [
      {
        name: '回放',
        path: 'RecordStream',
        component: RecordStream
      }
    ]
  },
  {
    name: '课程管理',
    path: 'Curriculum',
    component: CurriculumList,
    children: [
      {
        name: '课堂作业',
        path: 'Work',
        component: ClassroomWork,
        children: [
          {
            name: '作业统计',
            path: 'Statistics',
            component: WorkStatistics,
            children: [
              {
                name: '作业详情',
                path: 'analysis',
                component: Workanalysis
              }
            ]
          },
          {
            name: '作业预览',
            path: 'Preview',
            component: Workpreview
          }
        ]
      },
      {
        name: '听课',
        path: 'Live',
        component: LiveStream
      },
      {
        name: '听课',
        path: 'Listen',
        component: Listenclass
      },
      {
        name: '课件资料',
        path: 'Data',
        component: Classdata
      },
      {
        name: '课程详情',
        path: 'Details',
        component: ClassDetails,
        children: [
          {
            name: '课堂学生',
            path: 'Student',
            component: ClassStudent
          }
        ]
      },
      {
        name: '课表管理',
        path: 'ClassSchedule',
        component: ClassScheduleList
      },
      {
        name: '超级助教',
        path: 'ClassManagement',
        component: ClassManagement
      },
    ]
  },
  {
    name: '评估教学管理',
    path: 'TeachingEvaluation',
    component: EvaluateManagement,
    children: [
      {
        name: '评价管理',
        path: 'Management',
        component: EvaluateManagement
      },
      {
        name: '消息管理',
        path: 'Message',
        component: InfoManagement
      },
      {
        name: '评价课程',
        path: 'Lesson',
        component: EvaluateCourses
      }
    ]
  },
  {
    name: '学情监控',
    path: 'Situation',
    component: Situation
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
  },

  

  
];

/**
 * 线上
 */
export const teaOnlineNav = {
  name: '教师',
  path: '/TeaOnline',
  children: [
    ...commonData,
    {
      name: '在线微课管理',
      path: 'SmallLesson',
      component: SmallLessonList,
      children: [
        {
          name: '添加微课',
          path: 'Add',
          component: SmallLessonEdit
        },
        {
          name: '编辑微课',
          path: 'Edit',
          component: SmallLessonEdit
        }
      ]
    }
  ]
};

/**
 * 线下
 */
export const teaOfflineNav = {
  name: '教师',
  path: '/TeaOffline',
  children: commonData
};
