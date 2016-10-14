package com.streamdata.apps.cryptochat.utils;

/**
 * MultiKey implementation for pair of strings
 * (used for Receiver-Target table)
 */
public class ReceiverTargetKey {

    private final String receiver;
    private final String target;

    private static final int prime = 31;

    public ReceiverTargetKey(String receiver, String target) {
        this.receiver = receiver;
        this.target = target;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = prime * result + ((receiver == null) ? 0 : receiver.hashCode());
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        ReceiverTargetKey other = (ReceiverTargetKey) obj;

        if (receiver == null) {
            if (other.receiver != null) {
                return false;
            }
        } else if (!receiver.equals(other.receiver)) {
            return false;
        }
        if (target == null) {
            if (other.target != null) {
                return false;
            }
        } else if (!target.equals(other.target)) {
            return false;
        }

        return true;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getTarget() {
        return target;
    }
}
