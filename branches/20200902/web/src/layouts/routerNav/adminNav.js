import AddClassSchedule from '../../pages/Admin/ClassSchedule/AddClassSchedule';
import ClassScheduleList from '../../pages/Admin/ClassSchedule/ClassScheduleList';
import ClassStatistics from '../../pages/Admin/ClassStatistics/ClassStatistics';
import StudentGrouping from '../../pages/Admin/ClassSchedule/StudentGrouping';
import SmallLessonList from '../../pages/Admin/SmallLesson/SmallLessonList';
import SmallLessonInfo from '../../pages/Admin/SmallLesson/SmallLessonInfo';
import DemandCourseStatistics from '../../pages/Admin/DemandCourseStatistics';

const commonData = [
	{
		name: '课表管理',
		path: 'ClassSchedule',
		component: ClassScheduleList,
		children: [
			{
				name: '编辑课程',
				path: ':SubjectId/Edit',
				component: AddClassSchedule
			},
			{
				name: '添加课程',
				path: 'Add',
				component: AddClassSchedule
			},
			{
				name: '课堂学生',
				path: 'StudentGrouping',
				component: StudentGrouping
			}
		]
	},
	{
		name: '课程统计',
		path: 'ClassStatistics',
		component: ClassStatistics
	},
	{
		name: '在线微课管理',
		path: 'SmallLessonList',
		component: SmallLessonList,
		children: [
			{
				name: '微课详情',
				path: ':WeiKeId/Info',
				component: SmallLessonInfo
			}
		]
	},
	{
		name: '点播课程统计',
		path: 'DemandCourseStatistics',
		component: DemandCourseStatistics
	},
];

/**
 * 线下
 */
export const adminOfflineNav = {
	name: '管理员',
	path: '/AdminOffline',
	children: [...commonData]
};

/**
 * 线下
 */
export const adminOnlineNav = {
	name: '管理员',
	path: '/AdminOnline',
	children: commonData
};
