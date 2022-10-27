package osp.Devices;
import java.util.*;
import osp.IFLModules.*;
import osp.Hardware.*;
import osp.Interrupts.*;
import osp.Threads.*;
import osp.Utilities.*;
import osp.Tasks.*;
import osp.Memory.*;
import osp.FileSys.*;

/**
    The disk interrupt handler.  When a disk I/O interrupt occurs,
    this class is called upon the handle the interrupt.

    @OSPProject Devices
*/
public class DiskInterruptHandler extends IflDiskInterruptHandler
{
    /** 
        Handles disk interrupts. 
        
        This method obtains the interrupt parameters from the 
        interrupt vector. The parameters are IORB that caused the 
        interrupt: (IORB)InterruptVector.getEvent(), 
        and thread that initiated the I/O operation: 
        InterruptVector.getThread().
        The IORB object contains references to the memory page 
        and open file object that participated in the I/O.
        
        The method must unlock the page, set its IORB field to null,
        and decrement the file's IORB count.
        
        The method must set the frame as dirty if it was memory write 
        (but not, if it was a swap-in, check whether the device was 
        SwapDevice)

        As the last thing, all threads that were waiting for this 
        event to finish, must be resumed.

        @OSPProject Devices 
    */
    public void do_handleInterrupt()
    {
        // your code goes here
        IORB currentIORB = (IORB) InterruptVector.getEvent();
        ThreadCB currentThread = InterruptVector.getThread();
        TaskCB currentTask = currentThread.getTask();
        PageTableEntry currentPage = currentIORB.getPage();
        FrameTableEntry currentFrame = currentPage.getFrame();
        OpenFile currentFile = currentIORB.getOpenFile();
        currentFile.decrementIORBCount();
        if (currentFile.closePending && currentFile.getIORBCount() == 0){
            currentFile.close();
        }
        currentPage.unlock();
        if (currentThread.getStatus() != ThreadKill){
            if (currentIORB.getDeviceID() == SwapDeviceID){
                currentFrame.setDirty(false);
            }
            else{
                currentFrame.setReferenced(true);
                if (currentIORB.getIOType() == FileRead){
                    currentFrame.setDirty(true);
                }
            }
        }
        if ((currentTask.getStatus() != TaskLive) && currentFrame.isReserved()){
            currentFrame.setUnreserved(currentTask);
        }
        currentIORB.notifyThreads();
        Device currentDevice = Device.get(currentIORB.getDeviceID());
        currentDevice.setBusy(false);
        currentIORB = currentDevice.dequeueIORB();
        if (currentIORB != null){
            currentDevice.startIO(currentIORB);
        }
        ThreadCB.dispatch();
    }


    /*
       Feel free to add methods/fields to improve the readability of your code
    */

}

/*
      Feel free to add local classes to improve the readability of your code
*/
