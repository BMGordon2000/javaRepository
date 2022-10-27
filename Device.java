package osp.Devices;

/**
    This class stores all pertinent information about a device in
    the device table.  This class should be sub-classed by all
    device classes, such as the Disk class.

    @OSPProject Devices
*/

import osp.IFLModules.*;
import osp.Interrupts.InterruptVector;
import osp.Threads.*;
import osp.Utilities.*;
import osp.Hardware.*;
import osp.Memory.*;
import osp.FileSys.*;
import osp.Tasks.*;

import javax.swing.text.Utilities;
import java.util.*;

public class Device extends IflDevice
{
    /**
        This constructor initializes a device with the provided parameters.
	As a first statement it must have the following:

	    super(id,numberOfBlocks);

	@param numberOfBlocks -- number of blocks on device

        @OSPProject Devices
    */
    public Device(int id, int numberOfBlocks)
    {
        super(id, numberOfBlocks);
        // your code goes here
        this.iorbQueue = new SortedQueue();
    }

    /**
       This method is called once at the beginning of the
       simulation. Can be used to initialize static variables.

       @OSPProject Devices
    */
    public static void init()
    {
        for (int i = 0; i < getTableSize(); i++) {
            Device device = get(i);

            if (device instanceof Device) {
                int numOfPlatters = ((Disk) device).getPlatters();
                int tracksPerPlatter = ((Disk) device).getTracksPerPlatter();
                int sectorsPerTrack = ((Disk) device).getSectorsPerTrack();
                int bytesPerSecter = ((Disk) device).getBytesPerSector();


                MyOut.print("osp.Devices", "init(): numOfPlatters[" + device.getID() + "] = " + numOfPlatters);
                MyOut.print("osp.Devices", "init(): tracksPerPlatter[" + device.getID() + "] = " + tracksPerPlatter);
                MyOut.print("osp.Devices", "init(): sectorsPerTrack[" + device.getID() + "] = " + sectorsPerTrack);
                MyOut.print("osp.Devices", "init(): bytesPerSecter[" + device.getID() + "] = " + bytesPerSecter);
            }
        }


        // your code goes here

    }

    /**
       Enqueues the IORB to the IORB queue for this device
       according to some kind of scheduling algorithm.
       
       This method must lock the page (which may trigger a page fault),
       check the device's state and call startIO() if the 
       device is idle, otherwise append the IORB to the IORB queue.

       @return SUCCESS or FAILURE.
       FAILURE is returned if the IORB wasn't enqueued 
       (for instance, locking the page fails or thread is killed).
       SUCCESS is returned if the IORB is fine and either the page was 
       valid and device started on the IORB immediately or the IORB
       was successfully enqueued (possibly after causing pagefault pagefault)
       
       @OSPProject Devices
    */
    public int do_enqueueIORB(IORB iorb)
    {
        // your code goes here
        int checkLock = iorb.getPage().lock(iorb);
        if (checkLock == FAILURE){
            return FAILURE;
        }
        if (iorb.getThread().getStatus() == ThreadKill){
            return FAILURE;
        }
        iorb.getOpenFile().incrementIORBCount();
        int blockNumber = iorb.getBlockNumber();
        int cylinderNumber = getCylinderNumber(blockNumber);
        iorb.setCylinder(cylinderNumber);
        if (!this.isBusy()){
            this.startIO(iorb);
        }
        if (this.isBusy()){
            ((SortedQueue)this.iorbQueue).append(iorb);
        }
        return SUCCESS;
    }

    public int getCylinderNumber(int blockNumber){
        int pageSize = (int)Math.pow(2, MMU.getVirtualAddressBits() - MMU.getPageAddressBits());
        int blocksPerTrack = (((Disk) this).getSectorsPerTrack() * ((Disk) this).getBytesPerSector()) / pageSize;
        int cylinderNumber = blockNumber / (blocksPerTrack * (((Disk) this).getPlatters()));
        return cylinderNumber;
    }

    /**
       Selects an IORB (according to some scheduling strategy)
       and dequeues it from the IORB queue.

       @OSPProject Devices
    */
    public IORB do_dequeueIORB()
    {
        // your code goes here
        IORB iorb = (IORB) InterruptVector.getEvent();
        int blockNumber = iorb.getBlockNumber();
        int cylinderNumber = getCylinderNumber(blockNumber);
        if (this.iorbQueue.isEmpty()){
            return null;
        }
        return (IORB) (((SortedQueue) iorbQueue).removeHead());

        // Code below doesnt work due to findNextKey method throwing class osp.Devices.IORB cannot be cast to class osp.Utilities.SortedItemInterface (osp.Devices.IORB and osp.Utilities.SortedItemInterface are in unnamed module of loader 'app')

        // SortedItemInterface nextItem = ((SortedQueue) iorbQueue).findNextKey(cylinderNumber);
        // SortedItemInterface previousItem = (SortedItemInterface) ((SortedQueue) iorbQueue).findPrevKey(cylinderNumber);
        // if (nextItem == null && previousItem == null){
        //      return null;
        // }
        // if (nextItem == null){
        //      return (IORB) ((SortedQueue) this.iorbQueue).remove(previousItem);
        // }
        // if (previousItem == null){
        //      return (IORB) ((SortedQueue) this.iorbQueue).remove(nextItem);
        // }

        // int nextKey = ((SortedItemInterface) nextItem).getKey();
        // int previousKey = ((SortedItemInterface) previousItem).getKey();
        // if ((nextKey - cylinderNumber) > (cylinderNumber - previousKey)){
        //     return (IORB) ((SortedQueue) this.iorbQueue).remove(previousItem);
        // }
        // if ((nextKey - cylinderNumber) < (cylinderNumber - previousKey)){
        //     return (IORB) ((SortedQueue) this.iorbQueue).remove(nextItem);
        // }
        // return null;
    }

    /**
        Remove all IORBs that belong to the given ThreadCB from 
	this device's IORB queue

        The method is called when the thread dies and the I/O 
        operations it requested are no longer necessary. The memory 
        page used by the IORB must be unlocked and the IORB count for 
	the IORB's file must be decremented.

	@param thread thread whose I/O is being canceled

        @OSPProject Devices
    */
    public void do_cancelPendingIO(ThreadCB thread)
    {
        // your code goes here
        Enumeration forward = ((SortedQueue) iorbQueue).forwardIterator();
        while (forward.hasMoreElements()){
            IORB iorb = (IORB) forward.nextElement();
            if (iorb.getThread().equals(thread)){
                iorb.getPage().unlock();
                iorb.getOpenFile().decrementIORBCount();
                ((SortedQueue) this.iorbQueue).remove(iorb);
                if (iorb.getOpenFile().closePending && (iorb.getOpenFile().getIORBCount() == 0)){
                    iorb.getOpenFile().close();
                }
            }
        }
    }

    /** Called by OSP after printing an error message. The student can
	insert code here to print various tables and data structures
	in their state just after the error happened.  The body can be
	left empty, if this feature is not used.
	
	@OSPProject Devices
     */
    public static void atError()
    {
        // your code goes here

    }

    /** Called by OSP after printing a warning message. The student
	can insert code here to print various tables and data
	structures in their state just after the warning happened.
	The body can be left empty, if this feature is not used.
	
	@OSPProject Devices
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
