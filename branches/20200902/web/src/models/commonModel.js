import * as services from '../services/commonServices';
import { getSessionStorage, setSessionStorage, getTokenByCookie } from 'utils/storage';
import { routerRedux } from 'dva/router';
import { message } from 'antd';
const TeacherOnlinePath = '/TeaOnline', TeacherOfflinePath = '/TeaOffline';
const AdminOnlinePath = '/AdminOnline', AdminOfflinePath = '/AdminOffline';
const StudentOnlinePath = '/StudentOnline', StudentOfflinePath = '/StudentOffline';
/** 线上路由集合 */
const OnlinePathList = [AdminOnlinePath, TeacherOnlinePath, StudentOnlinePath, '/AccessOnline'];
/** 线下路由集合 */
const OfflinePathList = [AdminOfflinePath, TeacherOfflinePath, StudentOfflinePath, '/AccessOffline'];
/** 不需要验证登录的路由集合 */
const NoLoginPathList = ['/AccessOnline', '/AccessOffline'];

export default {
	namespace: 'commonModel',
	state: {
		/**
		 * 当前用户系统，1：线上；2：线下
		 */
		CurUserSystem: 1,
		//1、PC  2、H5
		FromTypeId: 1,
		/**
		 * 当前系统是线上还是线下path
		 */
		TeaBasePath: TeacherOnlinePath,
		/**
		 * 管理员系统线上/线下
		 */
		AdminBasePath: AdminOnlinePath,
		/**
		 * 学生端系统线上/线下
		 */
		StudentBasePath: StudentOnlinePath,
		/**
		 * 用户基本信息
		 */
		UserBaseInfo: null/* {
			UserRole: "", //用户角色
			UserFace: "", //用户头像
			UserName: "", //用户名
			UserId: "", //用户加密id
			UserTrueName: "", //姓名
		} */,
		//年级列表
		GradeList: [],
		//学科
		CourseList: [],
		//学段
		CourseTypesList: [],
		//学科状态
		SubjectStatusList: [],
		/**
		 * 未读数量
		 */
		UnReadCount: 0,
		/**
		 * 麵包屑最后一個的標題文字
		 */
		LastBreadcrumbName: null,
		/**系统设置公用图片和文字(默认线上) */
		SystemTheme: SYSTEM_CONFIG.online,

		/**云平台跳转地址 */
		DledcPlatformUrl: "",
		Collapsed: false,
	},

	reducers: {
		/**
		 *
		 * @param {Object} state 所有状态
		 * @param {Object} 要修改的CurUserRole状态
		 */
		updateUserSystem(state, { payload }) {
			state.CurUserSystem = payload;
			state.TeaBasePath = payload === 1 ? TeacherOnlinePath : TeacherOfflinePath;
			state.AdminBasePath = payload === 1 ? AdminOnlinePath : AdminOfflinePath;
			state.StudentBasePath = payload === 1 ? StudentOnlinePath : StudentOfflinePath;
			state.SystemTheme = payload === 1 ? SYSTEM_CONFIG.online : SYSTEM_CONFIG.offline; // 给主题赋值(区分线上线下)
			/**给线上线下登出路径赋值 */
			const link = payload === 1 ? SYSTEM_CONFIG.onlineLogoutLink : SYSTEM_CONFIG.offlineLogoutLink;
			if (process.env.UMIS_ENV === 'prod') {
				state.SystemTheme.logoutLocation = link.prod;  //线上环境
			} else if (process.env.UMIS_ENV === 'test') {
				state.SystemTheme.logoutLocation = link.test;  //测试环境
			} else if (process.env.UMIS_ENV === 'dev') {
				state.SystemTheme.logoutLocation = link.dev;  //本地环境
			}
			/**跳转云平台线上线下地址配置 */
			state.DledcPlatformUrl = payload === 1 ? SYSTEM_CONFIG.cloudPlatformLink.onlineUrl : SYSTEM_CONFIG.cloudPlatformLink.offlineUrl;
			return state;
		},
		/**
			 * 更新学科，年级，状态等数据
			 */
		updateCourseGroupTypeList(state, { payload }) {
			const CourseGroupTypeList = {
				CourseTypesList: payload.CourseTypesList,
				GradeList: payload.GradeList,
				CourseList: payload.CourseList,
				SubjectStatusList: payload.SubjectStatusList
			};
			return {
				...state,
				...CourseGroupTypeList
			};
		},
		changeCollapsed(state, { payload }) {
			return {
				...state,
				Collapsed: payload
			};
		},
		updateUnReadCount(state, { payload }) {
			return {
				...state,
				UnReadCount: payload
			};
		},
		/**
		 *
		 * @param {Object} state 所有状态
		 * @param {Object} 要修改的用户数据
		 */
		updateUserBaseInfo(state, { payload }) {
			return {
				...state,
				UserBaseInfo: payload
			};
		},
		/**
		 *
		 * @param {Object} state 所有状态
		 * @param {Object} 要修改的用户数据
		 */
		updateBreadCrumbName(state, { payload }) {
			return {
				...state,
				LastBreadcrumbName: payload
			};
		}
	},

	effects: {
		/**
	 * 获取学科/学段
	 */
		*getCourseGroupTypeList({ }, { call, put, all }) {
			let commonData = getSessionStorage('BaseClassList'),
				GradeList,
				CourseTypesList,
				SubjectStatusList,
				CourseList;

			if (
				commonData === null ||
				!commonData.GradeList ||
				!commonData.CourseTypesList ||
				!commonData.CourseList ||
				!commonData.SubjectStatusList
			) {
				const [CourseGroupsEntity, SubjectStatusEntity] = yield all([
					call(services.GetGradeList),
					call(services.GetSubjectStatusList)
				]);

				CourseTypesList = CourseGroupsEntity.CourseTypeList;
				GradeList = CourseGroupsEntity.GradeList;
				CourseList = CourseGroupsEntity.CourseList;
				SubjectStatusList = SubjectStatusEntity;

				commonData = {
					CourseList,
					CourseTypesList,
					GradeList,
					SubjectStatusList
				};
				setSessionStorage('BaseClassList', commonData);
			}
			yield put({
				type: 'updateCourseGroupTypeList',
				payload: commonData
			});
		},

		/**
		 * 获取未读消息数量
		 */
		*getUnReadMessageNumber({ payload }, { call, put, all }) {
			const result = yield call(services.GetUnReadMessageNumber, payload);
			yield put({
				type: 'updateUnReadCount',
				payload: result.UnReadMessageNumber
			})
		},

		/**
		 * 获取消息列表 学生端与教师端
		 * @param {*} param0
		 * @param {*} param1
		 */
		*GetMessageList({ payload }, { call }) {
			return yield call(services.GetMessageList, payload);
		},

		/**
		 * 获取个人信息
		 * @param {*} param0
		 * @param {*} param1
		 */
		*GetUserInfo({ payload }, { call }) {
			return yield call(services.GetUserInfo, payload);
		},
		/**
		 * 获取用户基本信息
		 * @param {*} param0
		 * @param {*} param1
		 */
		*getUserBaseInfoAsync({ payload }, { call }) {
			return yield call(services.GetUserBaseInfo, payload);
		},
		/**
		 * 获取用户基本信息（右上角或全局使用）
		 */
		*getUserBaseInfo({ payload }, { call, put, select }) {
			//缓存数据
			let userBaseInfo = getSessionStorage('UserBaseInfo');
			//当前Model存储的状态
			let sessionInfo = yield select(function (state) {
				return state.commonModel.UserBaseInfo;
			});
			if (!userBaseInfo || (payload && payload.IsRest)) {
				var result = yield call(services.GetUserBaseInfo);
				userBaseInfo = result;
				sessionInfo = null;
				setSessionStorage('UserBaseInfo', userBaseInfo);
			}
			if (!sessionInfo) {
				sessionInfo = userBaseInfo;
			}
			yield put({
				type: 'updateUserBaseInfo',
				payload: sessionInfo
			});
			return sessionInfo;
		},
		/**
		 * 双师账号密码登录
		 */
		*LoginByPassword({ payload }, { call }) {
			return yield call(services.LoginByPassword, payload);
		},

		/**
		 * 模拟账号密码云平台登录
		 */
		*LoginCloudplatform({ payload }, { call }) {
			return yield call(services.LoginCloudplatform, payload);
		},

		/**
		 * 云平台登录
		 */
		*LoginByCode({ payload }, { call }) {
			return yield call(services.LoginByCode, payload);
		},

		/**
		 * 标记已读消息
		 */
		*SignReadMessage({ payload }, { call }) {
			return yield call(services.SignReadMessage, payload);
		},
		/**
			 * 更新学生是否出席课程
			 */
		*UpdateSubjectStudentStatus({ payload }, { call }) {
			return yield call(services.UpdateSubjectStudentStatus, payload);
		},
		/**
	 	* 未登录用户获取学科/学段
	 	*/
		*getCourseGroupTypeListNoLogin({ }, { call, put, all }) {
			let commonData = getSessionStorage('BaseClassList'),
				GradeList,
				CourseTypesList,
				CourseList;

			if (
				commonData === null ||
				!commonData.GradeList ||
				!commonData.CourseTypesList ||
				!commonData.CourseList
			) {
				const [CourseGroupsEntity] = yield all([
					call(services.GetGradeList)
				]);

				CourseTypesList = CourseGroupsEntity.CourseTypeList;
				GradeList = CourseGroupsEntity.GradeList;
				CourseList = CourseGroupsEntity.CourseList;

				commonData = {
					CourseList,
					CourseTypesList,
					GradeList,
				};
				setSessionStorage('BaseClassList', commonData);
			}
			yield put({
				type: 'updateCourseGroupTypeList',
				payload: commonData
			});

			return true;
		},
		/**
		 * 获取微课状态列表
		 */
		*GetWeiKeStatusList({ payload }, { call }) {
			return yield call(services.GetWeiKeStatusList, payload);
		},
		/**
		 * 播放统计接口
		 */
		*RecordLog({ payload }, { call }) {
			return yield call(services.RecordLog, payload);
		},
	},
	subscriptions: {
		setup({ history, dispatch }) {
			return history.listen(location => {
				const pathname = location.pathname;
				/** 判断用户是否登录 */
				if (pathname !== '/' && NoLoginPathList.filter(p => pathname.indexOf(p) > -1).length === 0) {
					if (!getTokenByCookie()) {
						//未登录，退出
						message.warn('请先登录');
						dispatch(routerRedux.push('/'));
					} else {
						dispatch({
							type: 'getUserBaseInfo'
						});
						dispatch({
							type: 'getUnReadMessageNumber'
						})
						/** 获取公共信息：年级、学段、学科 */
						dispatch({ type: 'getCourseGroupTypeList' });
					}
				}

				/** 根据路由修改系统为线上、线下 */
				if (OnlinePathList.find(p => pathname.indexOf(p) > -1) != null) {
					dispatch({
						type: 'updateUserSystem',
						payload: 1
					});
				}
				if (OfflinePathList.find(p => pathname.indexOf(p) > -1) != null) {
					dispatch({
						type: 'updateUserSystem',
						payload: 2
					});
				}
			});
		}
	}
};
