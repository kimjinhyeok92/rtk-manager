/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */

// MESSAGE SCENARIO_CMD PACKING
package com.MAVLink.swarm;
import com.MAVLink.MAVLinkPacket;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.Messages.Units;
import com.MAVLink.Messages.Description;

/**
 * scenario_cmd
 */
public class msg_scenario_cmd extends MAVLinkMessage {

    public static final int MAVLINK_MSG_ID_SCENARIO_CMD = 181;
    public static final int MAVLINK_MSG_LENGTH = 50;
    private static final long serialVersionUID = MAVLINK_MSG_ID_SCENARIO_CMD;

    
    /**
     * param1
     */
    @Description("param1")
    @Units("")
    public float param1;
    
    /**
     * param2
     */
    @Description("param2")
    @Units("")
    public float param2;
    
    /**
     * param3
     */
    @Description("param3")
    @Units("")
    public float param3;
    
    /**
     * param4
     */
    @Description("param4")
    @Units("")
    public long param4;
    
    /**
     * target system id
     */
    @Description("target system id")
    @Units("")
    public short target_system;
    
    /**
     * command
     */
    @Description("command")
    @Units("")
    public short cmd;
    
    /**
     * param5
     */
    @Description("param5")
    @Units("")
    public short param5[] = new short[32];
    

    /**
     * Generates the payload for a mavlink message for a message of this type
     * @return
     */
    @Override
    public MAVLinkPacket pack() {
        MAVLinkPacket packet = new MAVLinkPacket(MAVLINK_MSG_LENGTH,isMavlink2);
        packet.sysid = sysid;
        packet.compid = compid;
        packet.msgid = MAVLINK_MSG_ID_SCENARIO_CMD;

        packet.payload.putFloat(param1);
        packet.payload.putFloat(param2);
        packet.payload.putFloat(param3);
        packet.payload.putUnsignedInt(param4);
        packet.payload.putUnsignedByte(target_system);
        packet.payload.putUnsignedByte(cmd);
        
        for (int i = 0; i < param5.length; i++) {
            packet.payload.putUnsignedByte(param5[i]);
        }
                    
        
        if (isMavlink2) {
            
        }
        return packet;
    }

    /**
     * Decode a scenario_cmd message into this class fields
     *
     * @param payload The message to decode
     */
    @Override
    public void unpack(MAVLinkPayload payload) {
        payload.resetIndex();

        this.param1 = payload.getFloat();
        this.param2 = payload.getFloat();
        this.param3 = payload.getFloat();
        this.param4 = payload.getUnsignedInt();
        this.target_system = payload.getUnsignedByte();
        this.cmd = payload.getUnsignedByte();
        
        for (int i = 0; i < this.param5.length; i++) {
            this.param5[i] = payload.getUnsignedByte();
        }
                
        
        if (isMavlink2) {
            
        }
    }

    /**
     * Constructor for a new message, just initializes the msgid
     */
    public msg_scenario_cmd() {
        this.msgid = MAVLINK_MSG_ID_SCENARIO_CMD;
    }

    /**
     * Constructor for a new message, initializes msgid and all payload variables
     */
    public msg_scenario_cmd( float param1, float param2, float param3, long param4, short target_system, short cmd, short[] param5) {
        this.msgid = MAVLINK_MSG_ID_SCENARIO_CMD;

        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.target_system = target_system;
        this.cmd = cmd;
        this.param5 = param5;
        
    }

    /**
     * Constructor for a new message, initializes everything
     */
    public msg_scenario_cmd( float param1, float param2, float param3, long param4, short target_system, short cmd, short[] param5, int sysid, int compid, boolean isMavlink2) {
        this.msgid = MAVLINK_MSG_ID_SCENARIO_CMD;
        this.sysid = sysid;
        this.compid = compid;
        this.isMavlink2 = isMavlink2;

        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.target_system = target_system;
        this.cmd = cmd;
        this.param5 = param5;
        
    }

    /**
     * Constructor for a new message, initializes the message with the payload
     * from a mavlink packet
     *
     */
    public msg_scenario_cmd(MAVLinkPacket mavLinkPacket) {
        this.msgid = MAVLINK_MSG_ID_SCENARIO_CMD;

        this.sysid = mavLinkPacket.sysid;
        this.compid = mavLinkPacket.compid;
        this.isMavlink2 = mavLinkPacket.isMavlink2;
        unpack(mavLinkPacket.payload);
    }

                  
    /**
     * Returns a string with the MSG name and data
     */
    @Override
    public String toString() {
        return "MAVLINK_MSG_ID_SCENARIO_CMD - sysid:"+sysid+" compid:"+compid+" param1:"+param1+" param2:"+param2+" param3:"+param3+" param4:"+param4+" target_system:"+target_system+" cmd:"+cmd+" param5:"+param5+"";
    }

    /**
     * Returns a human-readable string of the name of the message
     */
    @Override
    public String name() {
        return "MAVLINK_MSG_ID_SCENARIO_CMD";
    }
}
        