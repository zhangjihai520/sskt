import dva from 'dva';
import createLoading from 'dva-loading';

const runtimeDva = window.g_plugins.mergeConfig('dva');
let app = dva({
  history: window.g_history,
  
  ...(runtimeDva.config || {}),
});

window.g_app = app;
app.use(createLoading());
(runtimeDva.plugins || []).forEach(plugin => {
  app.use(plugin);
});

app.model({ namespace: 'classScheduleModel', ...(require('E:/project/sskt/trunk/web/src/models/classScheduleModel.js').default) });
app.model({ namespace: 'classStatisticsModel', ...(require('E:/project/sskt/trunk/web/src/models/classStatisticsModel.js').default) });
app.model({ namespace: 'commonModel', ...(require('E:/project/sskt/trunk/web/src/models/commonModel.js').default) });
app.model({ namespace: 'curriculumModel', ...(require('E:/project/sskt/trunk/web/src/models/curriculumModel.js').default) });
app.model({ namespace: 'smallModel', ...(require('E:/project/sskt/trunk/web/src/models/smallModel.js').default) });
app.model({ namespace: 'studentPersonalModel', ...(require('E:/project/sskt/trunk/web/src/models/studentPersonalModel.js').default) });
app.model({ namespace: 'stuModel', ...(require('E:/project/sskt/trunk/web/src/models/stuModel.js').default) });
app.model({ namespace: 'teachingEvaluationModel', ...(require('E:/project/sskt/trunk/web/src/models/teachingEvaluationModel.js').default) });
