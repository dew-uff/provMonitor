package br.uff.ic.provmonitor.business;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import br.uff.ic.provmonitor.cvsmanager.CVSManager;
import br.uff.ic.provmonitor.cvsmanager.CVSManagerFactory;
import br.uff.ic.provmonitor.dao.ArtifactInstanceDAO;
import br.uff.ic.provmonitor.dao.ExecutionStatusDAO;
import br.uff.ic.provmonitor.dao.factory.ProvMonitorDAOFactory;
import br.uff.ic.provmonitor.exceptions.ProvMonitorException;
import br.uff.ic.provmonitor.model.ArtifactInstance;
import br.uff.ic.provmonitor.model.ExecutionFilesStatus;
import br.uff.ic.provmonitor.model.ExecutionStatus;
import br.uff.ic.provmonitor.workspaceWatcher.AccessedPath;
import br.uff.ic.provmonitor.workspaceWatcher.WorkspaceAccessReader;

//TODO: Translate comments and Javadocs
public class RetrospectiveProvenanceBusinessServices {
		
	//private Repository repository;
	
	//public RetrospectiveProvenanceServices() {
		//repository = Repository.getInstance();
	//}
	
	/**
	 * Inicializa uma nova execu��o de um experimento.
	 * @param experimentId Identificador do experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public String initializeExperimentExecution(String experimentId) throws CharonException{
		//return repository.getCharon().getCharonAPI().initializeExperimentExecution(experimentId);
	//}
	public static void initializeExperimentExecution(String experimentId) throws ProvMonitorException{
		//System.out.println("initializeExperimentExecution start execution...");
		
		//Record Timestamp
		Date timeStampInitExecute = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(timeStampInitExecute);
		String experimentInstanceId = experimentId + nonce;
		
		//Printing Generated Values
		System.out.println("ExperimentInstanceId: " + experimentInstanceId);
		System.out.println("BranchName: Branch_" + experimentInstanceId);
		
		//Initialize DB
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbInitialize();
		
		//Repository clone
		
		//Repository Branch
		
		//System.out.println("initializeExperimentExecution end execution.");
	}
	
	/**
	 * Inicializa uma nova execu��o de um experimento.
	 * @param experimentId Identificador do experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public String initializeExperimentExecution(String experimentId) throws CharonException{
		//return repository.getCharon().getCharonAPI().initializeExperimentExecution(experimentId);
	//}
	public static void initializeExperimentExecution(String experimentId, String sourceRepository, String workspacePath) throws ProvMonitorException{
		//System.out.println("initializeExperimentExecution start execution...");
		
		//Record Timestamp
		Date timeStampInitExecute = Calendar.getInstance().getTime();
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMddHHmmssS");
		String nonce = sf.format(timeStampInitExecute);
		String experimentInstanceId = experimentId + nonce;
		
		//Printing Generated Values
		System.out.println("ExperimentInstanceId: " + experimentInstanceId);
		System.out.println("BranchName: Branch_" + experimentInstanceId);
		
		System.out.println("Workspace: " + workspacePath);
		System.out.println("CentralRepository: " + sourceRepository);
		
		//Initialize DB
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbInitialize();
		
		//Repository clone
		CVSManager cvsManager = CVSManagerFactory.getInstance();
		//System.out.println("Cloning to: " + workspacePath);
		cvsManager.cloneRepository(sourceRepository, workspacePath);
		
		//Repository branch
		//cvsManager.createBranch(workspacePath, experimentInstanceId);
		
		//Repository checkOut
		//cvsManager.checkout(workspacePath, experimentInstanceId);
		
		//System.out.println("initializeExperimentExecution end execution.");
	}
	
	public static void FinalizeExperimentExecution(String experimentInstanceId, String centralRepository, Date endDateTime) throws ProvMonitorException{
		//Stop DB infra
		ProvMonitorDAOFactory daoFactory = new ProvMonitorDAOFactory();
		daoFactory.getDatabaseControlDAO().dbFinalize();
		
		//Pushback Repository
		
	}
	
	/**
	 * Notifica o in�cio de execu��o de uma inst�ncia de uma atividade simples.
	 * @param activityInstanceId Identificador da inst�ncia da atividade simples que foi inicializada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade simples no experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean notifyActivityExecutionStartup(String activityInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyActivityExecutionStartup(activityInstanceId, context);
	//}
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context){
		//Record Timestamp
		
	}
	
	/**
	 * Notifica o in�cio de execu��o de uma inst�ncia de uma atividade simples.
	 * @param activityInstanceId Identificador da inst�ncia da atividade simples que foi inicializada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade simples no experimento.
	 * @param activityStartDateTime
	 * @return resposta de sucesso de opera��o (true|false).'
	 * @throws ProvMonitorException
	 */
	public static void notifyActivityExecutionStartup(String activityInstanceId, String[] context, Date activityStartDateTime, String workspacePath) throws ProvMonitorException{
		try{
			//Prepare ActivityObject to be persisted
			ExecutionStatus elementExecStatus = new ExecutionStatus();
			elementExecStatus.setElementId(activityInstanceId);
			elementExecStatus.setElementType("activity");
			elementExecStatus.setStatus("starting");
			//Mounting context
			StringBuilder elementPath = new StringBuilder();
			for (String path: context){
				if (elementPath.length()>0){
					elementPath.append("\\");
				}
				elementPath.append(path);
			}
			
			elementExecStatus.setElementPath(elementPath.toString());
			//Record Timestamp
			elementExecStatus.setStartTime(activityStartDateTime);
			
			//ActivityInstance activity = new ActivityInstance();
			//activity.setActivityInstanceId(activityInstanceId);
			//activity.set
			
			
			//Reading accessedFiles
			ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
			Collection<AccessedPath> accessedFiles = WorkspaceAccessReader.readAccessedPathsAndAccessTime(Paths.get(workspacePath), activityStartDateTime, true);
			if (accessedFiles != null && !accessedFiles.isEmpty()){
				for (AccessedPath acFile: accessedFiles){
					
					ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
					execFileStatus.setFileAccessDateTime(acFile.getAccessedDateTime());
					execFileStatus.setFilePath(acFile.getAccessedPathName());
					execFileStatus.setElementId(activityInstanceId);
					execFileStatus.setElementPath(elementPath.toString());
					execFileStatus.setFiletAccessType(ExecutionFilesStatus.TYPE_READ);
					
					execFiles.add(execFileStatus);
					
				}
			}
			
			
			//Activity start commit
			StringBuilder message = new StringBuilder();
			message.append("ActivityInstanceId:")
				   .append(activityInstanceId)
				   .append("; context:")
				   .append(elementPath.toString())
				   .append("; StartActivityCommit");
			
			CVSManager cvsManager = CVSManagerFactory.getInstance();
			cvsManager.addAllFromPath(workspacePath);
			String commitId = cvsManager.commit(workspacePath, message.toString());
			
			//Recording Commit ID
			elementExecStatus.setCommitId(commitId);
			
			
			
			ProvMonitorDAOFactory factory = new ProvMonitorDAOFactory();
			//factory.getActivityInstanceDAO().persist(activityInstance);
			factory.getExecutionStatusDAO().persist(elementExecStatus);
			
		}catch(IOException e){
			throw new ProvMonitorException(e.getMessage(), e.getCause());
		}
		
	}
	
	/**
	 * Notifica o in�cio de execu��o de uma inst�ncia de uma atividade composta.
	 * @param processInstanceId Identificador da inst�ncia da atividade composta que foi inicializada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade composta no experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean notifyProcessExecutionStartup(String processInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyProcessExecutionStartup(processInstanceId, context);
	//}
	public static void notifyProcessExecutionStartup(String processInstanceId, String[] context){
	}
	
	/**
	 * Notifica o in�cio de execu��o de uma inst�ncia de uma atividade composta.
	 * @param processInstanceId Identificador da inst�ncia da atividade composta que foi inicializada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade composta no experimento.
	 * @param processStartDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyProcessExecutionStartup(String processInstanceId, String[] context, Date processStartDateTime, String workspacePath) throws ProvMonitorException{
		notifyActivityExecutionStartup(processInstanceId, context, processStartDateTime, workspacePath);
	}
	
	/**
	 * Notifica o fim de execu��o de uma inst�ncia de uma atividade simples.
	 * @param activityInstanceId Identificador da inst�ncia da atividade simples que foi finalizada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade simples no experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean notifyActivityExecutionEnding(String activityInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyActivityExecutionEnding(activityInstanceId, context);
	//}
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context){
		//Record Timestamp
		
		//Verify accessed files
		
		//Commit changed files
	}
	
	/**
	 * Notifica o fim de execu��o de uma inst�ncia de uma atividade simples.
	 * @param activityInstanceId Identificador da inst�ncia da atividade simples que foi finalizada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade simples no experimento.
	 * @param startActivityDateTime
	 * @param endActiviyDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyActivityExecutionEnding(String activityInstanceId, String[] context, Date startActivityDateTime, Date endActiviyDateTime, String workspacePath) throws ProvMonitorException{
		//Mounting context
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("\\");
			}
			elementPath.append(path);
		}
		
		//Record Timestamp
		
		//Verify accessed files
		try{
			ArrayList<ExecutionFilesStatus> execFiles = new ArrayList<ExecutionFilesStatus>();
			Collection<AccessedPath> accessedFiles = WorkspaceAccessReader.readAccessedPathsAndAccessTime(Paths.get(workspacePath), startActivityDateTime, true);
			if (accessedFiles != null && !accessedFiles.isEmpty()){
				for (AccessedPath acFile: accessedFiles){
					
					ExecutionFilesStatus execFileStatus = new ExecutionFilesStatus();
					execFileStatus.setFileAccessDateTime(acFile.getAccessedDateTime());
					execFileStatus.setFilePath(acFile.getAccessedPathName());
					execFileStatus.setElementId(activityInstanceId);
					execFileStatus.setElementPath(elementPath.toString());
					execFileStatus.setFiletAccessType(ExecutionFilesStatus.TYPE_READ);
					
					execFiles.add(execFileStatus);
					
				}
			}
		}catch(IOException e){
			throw new ProvMonitorException(e.getMessage(), e.getCause());
		}
				
				
		//Commit changed files
		StringBuilder message = new StringBuilder();
		message.append("ActivityInstanceId:")
			   .append(activityInstanceId)
			   .append("; context:")
			   .append(elementPath.toString())
			   .append("; EndActivityCommit");
		
		CVSManager cvsManager = CVSManagerFactory.getInstance();
		cvsManager.addAllFromPath(workspacePath);
		String commitId = cvsManager.commit(workspacePath, message.toString());
				
		
		//Recover executionStatus element
		System.out.println("Starting ActivityExecutionEnding Method...");
		ExecutionStatusDAO execStatusDAO = new ProvMonitorDAOFactory().getExecutionStatusDAO();
		System.out.println("Getting activity by id: " + activityInstanceId);
		ExecutionStatus elemExecutionStatus = execStatusDAO.getById(activityInstanceId, elementPath.toString());
		
		if (elemExecutionStatus == null){
			throw new ProvMonitorException("Element: " + activityInstanceId + " not found exception. Activity could not be finished if it was not started." );
		}
		
		//update execution element
		System.out.println("Updating Activity properties: End DateTime....");
		elemExecutionStatus.setEndTime(endActiviyDateTime);
		
		System.out.println("Updating Activity properties: Status....");
		elemExecutionStatus.setStatus("ended");
		
		//Recording Commit ID
		elemExecutionStatus.setCommitId(commitId);
		
		//persist updated element
		System.out.println("Persisting Activity....");
		execStatusDAO.update(elemExecutionStatus);
		System.out.println("Activity Persisted.");
		
	}
	
	/**
	 * Notifica o fim de execu��o de uma inst�ncia de uma atividade composta.
	 * @param processInstanceId Identificador da inst�ncia da atividade composta que foi finalizada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade composta no experimento.
	 * @param startProcessDateTime
	 * @param endProcessDateTime
	 * @throws ProvMonitorException
	 */
	public static void notifyProcessExecutionEnding(String processInstanceId, String[] context, Date startProcessDateTime, Date endProcessDateTime, String workspacePath) throws ProvMonitorException{
		//Record Timestamp
		//Verify accessed files
		//Commit changed files
		notifyActivityExecutionEnding(processInstanceId, context, startProcessDateTime, endProcessDateTime, workspacePath);
	}
	
	/**
	 * Notifica o fim de execu��o de uma inst�ncia de uma atividade composta.
	 * @param processInstanceId Identificador da inst�ncia da atividade composta que foi finalizada.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o da atividade composta no experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean notifyProcessExecutionEnding(String processInstanceId, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyProcessExecutionEnding(processInstanceId, context);
	//}
	public static void notifyProcessExecutionEnding(String processInstanceId, String[] context){
		//Record Timestamp
		
		//Verify accessed files
		
		//Commit changed files
	}
	
	/**
	 * Notifica o fim de execu��o de um ponto de decis�o.
	 * @param decisionPointId Identificador do ponto de decis�o que foi finalizado.
	 * @param optionValue Op��o selecionada para o ponto de decis�o.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o do ponto de decis�o no experimento.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean notifyDecisionPointEnding(String decisionPointId, String optionValue, String[] context) throws CharonException{
		//return repository.getCharon().getCharonAPI().notifyDecisionPointEnding(decisionPointId, optionValue, context);
	//}
	public static void notifyDecisionPointEnding(String decisionPointId, String optionValue, String[] context){
	}

	/**
	 * Publica os dados do artefato.
	 * @param artifactId Identificador do artefato.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o do artefato no experimento.
	 * @param value Valor do artefato.
	 * @throws ProvMonitorException
	 */
	//public boolean setArtifactValue(String artifactId, String[] context, String value) throws CharonException{
		//return repository.getCharon().getCharonAPI().setArtifactValue(artifactId, context, value);
	//}
	public static void setArtifactValue(String artifactId, String[] context, String value) throws ProvMonitorException{
		StringBuilder elementPath = new StringBuilder();
		for (String path: context){
			if (elementPath.length()>0){
				elementPath.append("\\");
			}
			elementPath.append(path);
		}
		
		//Preparing artifact Objetct to be persisted
		ArtifactInstance artifactInstance = new ArtifactInstance();
		artifactInstance.setArtifactId(artifactId);
		artifactInstance.setArtifactValue(value);
		artifactInstance.setArtifactPath(elementPath.toString());
		
		ArtifactInstanceDAO artifactValueDAO = new ProvMonitorDAOFactory().getArtifactInstanceDAO();
		artifactValueDAO.persist(artifactInstance);
		
	}
	
	
	/**
	 * Publica a localiza��o dos dados do artefato.
	 * @param artifactId Identificador do experimento.
	 * @param context Sequ�ncia de indentificadores que define a localiza��o do artefato no experimento.
	 * @param hostURL URL da m�quina onde o artefato est� localizado.
	 * @param hostLocalPath Diret�rio local da m�quina onde o artefato est� localizado.
	 * @return resposta de sucesso de opera��o (true|false).
	 */
	//public boolean publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath) throws CharonException{
		//return repository.getCharon().getCharonAPI().publishArtifactValueLocation(artifactId, context, hostURL, hostLocalPath);
	//}
	public static void publishArtifactValueLocation(String artifactId, String[] context, String hostURL, String hostLocalPath){
		
	}
	
}

