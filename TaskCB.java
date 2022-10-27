package osp;

import java.util.Vector;
import osp.IFLModules.*;
import osp.Threads.*;
import osp.Ports.*;
import osp.Memory.*;
import osp.FileSys.*;
import osp.Hardware.*;

/**
    The student module dealing with the creation and killing of
    tasks.  A task acts primarily as a container for threads and as
    a holder of resources.  Execution is associated entirely with
    threads.  The primary methods that the student will implement
    are do_create(TaskCB) and do_kill(TaskCB).  The student can choose
    how to keep track of which threads are part of a task.  In this
    implementation, an array is used.

    @OSPProject Tasks
*/
public class TaskCB extends IflTaskCB
{
    /**
       The task constructor. Must have

       	   super();

       as its first statement.

       @OSPProject Tasks
    */
    Vector threadList = new Vector();
    Vector portList = new Vector();
    Vector openFileList = new Vector();

    public TaskCB()
    {
        // your code goes here
        super();

    }

    /**
       This method is called once at the beginning of the
       simulation. Can be used to initialize static variables.

       @OSPProject Tasks
    */
    public static void init()
    {
        // your code goes here

    }

    /** 
        Sets the properties of a new task, passed as an argument. 
        
        Creates a new thread list, sets TaskLive status and creation time,
        creates and opens the task's swap file of the size equal to the size
	(in bytes) of the addressable virtual memory.

	@return task or null

        @OSPProject Tasks
    */
    static public TaskCB do_create()
    {
        // your code goes here
        TaskCB newTask = new TaskCB();
        PageTable newPageTable = new PageTable(newTask);
        newTask.setPageTable(newPageTable);
        newTask.setCreationTime(HClock.get());
        newTask.setPriority(1);
        newTask.setStatus(TaskLive);
        String taskID = newTask.getID();
        String DeviceMount = SwapDeviceMountPoint;
        String name = DeviceMount + taskID;
        int fileSize = Math.pow(2, MMU.getVirtualAddressBits());
        FileSys.create(name, fileSize);
        OpenFile swapFile = OpenFile.open(name, newTask);
        if (swapFile == null){
            ThreadCB.dispatch();
            return null;
        }
        else {
            newTask.setSwapFile(swapFile);
            ThreadCB.create(newTask);
            return newTask;
        }
    }

    /**
       Kills the specified task and all of it threads. 

       Sets the status TaskTerm, frees all memory frames 
       (reserved frames may not be unreserved, but must be marked 
       free), deletes the task's swap file.
	
       @OSPProject Tasks
    */
    public void do_kill()
    {
        // your code goes here
        for (int a = threadList.size(); a = 0; --a){
            ThreadCB.kill(threadList.get(a));
        }
        for (int b = portList.size(); b = 0; --b){
            PortCB.destroy(threadList.get(b));
        }
        this.setStatus(TaskTerm);
        this.getPageTable().deallocateMemory();
        for (int c = openFileList.size(); c = 0; --c){
            OpenFile.close(openFileList.get(c));
        }

    }

    /** 
	Returns a count of the number of threads in this task. 
	
	@OSPProject Tasks
    */
    public int do_getThreadCount()
    {
        // your code goes here
        return threadList.size();
    }

    /**
       Adds the specified thread to this task. 
       @return FAILURE, if the number of threads exceeds MaxThreadsPerTask;
       SUCCESS otherwise.
       
       @OSPProject Tasks
    */
    public int do_addThread(ThreadCB thread)
    {
        // your code goes here
        if (ThreadCB.MaxThreadsPerTask <= do_getThreadCount()) {
            return FAILURE;
        }
        else{
            threadList.addElement(thread);
            return SUCCESS;
        }
    }

    /**
       Removes the specified thread from this task. 		

       @OSPProject Tasks
    */
    public int do_removeThread(ThreadCB thread)
    {
        // your code goes here
        if (threadList.contains(thread) == true){
            threadlist.remove(thread);
            return SUCCESS;
        }
        else{
            return FAILURE;
        }
    }

    /**
       Return number of ports currently owned by this task. 

       @OSPProject Tasks
    */
    public int do_getPortCount()
    {
        // your code goes here
        return portList.size();
    }

    /**
       Add the port to the list of ports owned by this task.
	
       @OSPProject Tasks 
    */ 
    public int do_addPort(PortCB newPort)
    {
        // your code goes here
        if (PortCB.MaxPortsPerTask <= do_getPortCount()){
            return FAILURE;
        }
        else{
            portList.addElement(newPort);
            return SUCCESS;
        }
    }

    /**
       Remove the port from the list of ports owned by this task.

       @OSPProject Tasks 
    */ 
    public int do_removePort(PortCB oldPort)
    {
        // your code goes here
        if (portList.contains(oldPort) == true){
            portList.remove(oldPort);
            return SUCCESS;
        }
        else{
            return FAILURE;
        }
    }

    /**
       Insert file into the open files table of the task.

       @OSPProject Tasks
    */
    public void do_addFile(OpenFile file)
    {
        // your code goes here
        openFileList.addElement(file);
    }

    /** 
	Remove file from the task's open files table.

	@OSPProject Tasks
    */
    public int do_removeFile(OpenFile file)
    {
        // your code goes here
        if (openFileList.contains(file) == true){
            openFileList.remove(file);
            return SUCCESS;
        }
        else{
            return FAILURE;
        }
    }

    /**
       Called by OSP after printing an error message. The student can
       insert code here to print various tables and data structures
       in their state just after the error happened.  The body can be
       left empty, if this feature is not used.
       
       @OSPProject Tasks
    */
    public static void atError()
    {
        // your code goes here

    }

    /**
       Called by OSP after printing a warning message. The student
       can insert code here to print various tables and data
       structures in their state just after the warning happened.
       The body can be left empty, if this feature is not used.
       
       @OSPProject Tasks
    */
    public static void atWarning()
    {
        // your code goes here

    }


    /*
       Feel free to add methods/fields to improve the readability of your code
    */

}

/*
      Feel free to add local classes to improve the readability of your code
*/
