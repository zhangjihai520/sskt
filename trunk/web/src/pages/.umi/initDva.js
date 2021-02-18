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

app.model({ namespace: 'classScheduleModel', ...(require('D:/project/trunk/web/src/models/classScheduleModel.js').default) });
app.model({ namespace: 'classStatisticsModel', ...(require('D:/project/trunk/web/src/models/classStatisticsModel.js').default) });
app.model({ namespace: 'commonModel', ...(require('D:/project/trunk/web/src/models/commonModel.js').default) });
app.model({ namespace: 'curriculumModel', ...(require('D:/project/trunk/web/src/models/curriculumModel.js').default) });
app.model({ namespace: 'smallModel', ...(require('D:/project/trunk/web/src/models/smallModel.js').default) });
app.model({ namespace: 'studentPersonalModel', ...(require('D:/project/trunk/web/src/models/studentPersonalModel.js').default) });
app.model({ namespace: 'stuModel', ...(require('D:/project/trunk/web/src/models/stuModel.js').default) });
app.model({ namespace: 'teachingEvaluationModel', ...(require('D:/project/trunk/web/src/models/teachingEvaluationModel.js').default) });
