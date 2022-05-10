package com.cc.flowable.component;

import com.cc.flowable.API.vo.HolidayReq;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wcc 2022/5/10
 */
@Slf4j
@Service
public class FlowableService {

    private ProcessEngine processEngine;

    public FlowableService(ProcessEngine processEngine) {
        this.processEngine = processEngine;
    }

    public Deployment classpathProcessDefinition(CommonProcess holidayRequest) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 流程部署
        return repositoryService.createDeployment()
                .addClasspathResource(holidayRequest.getData())
                .deploy();
    }

    public ProcessDefinition restAPI(Deployment deployment) {
        RepositoryService repositoryService = processEngine.getRepositoryService();

        return repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();

    }

    public ProcessInstance holiday(HolidayReq req) {
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("employee", req.getEmployee());
        variables.put("nrOfHolidays", req.getHolidays());
        variables.put("reason", req.getReason());
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey(CommonProcess.holidayRequest.getId(), variables);
        return processInstance;
    }

    public void processTask() {
        TaskService taskService = processEngine.getTaskService();
        String managers = "managers";
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(managers).list();
        log.info("{} have {} tasks:", managers, tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            log.info("the {} task. executionId={},processInstanceId={},processDefinitionId={},taskKey={},id={}", i + 1,
                    task.getExecutionId(), task.getProcessInstanceId(), task.getProcessDefinitionId(),
                    task.getTaskDefinitionKey(), task.getId());
            Map<String, Object> processVariables = taskService.getVariables(task.getId());
            log.info(processVariables.get("employee") + " wants " +
                            processVariables.get("nrOfHolidays") + " of holidays. Do you approve this? reason :{}",
                    processVariables.get("reason"));

            agree(taskService, task);

        }

    }

    private void agree(TaskService taskService, Task task) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", true);
        taskService.complete(task.getId(), variables);
    }

    public void history(ProcessInstance instance) {
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(instance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities) {
            log.info(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");
        }

    }
}
