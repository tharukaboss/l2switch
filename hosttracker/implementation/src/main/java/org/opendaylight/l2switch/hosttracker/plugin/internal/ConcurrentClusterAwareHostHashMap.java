/**
 * Copyright (c) 2014 André Martins and others. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.l2switch.hosttracker.plugin.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.opendaylight.controller.md.sal.binding.api.ReadWriteTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.l2switch.hosttracker.plugin.inventory.Host;
import org.opendaylight.l2switch.hosttracker.plugin.util.Utilities;
import org.opendaylight.yang.gen.v1.urn.opendaylight.host.tracker.rev140624.HostId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This will (try to) submit all writes and deletes in to the MD-SAL database.
 * The
 * {@link #removeLocally(org.opendaylight.yangtools.yang.binding.InstanceIdentifier)}
 * , {@link #removeLocally(java.lang.Object) }
 * {@link #putLocally(org.opendaylight.yangtools.yang.binding.InstanceIdentifier, java.lang.Object)}
 * methods should be used when dataChanges are dealt locally and not update to
 * MD-SAL.
 *
 * @param <K>
 *            Must be a HostId
 * @param <V>
 *            Must be
 *            org.opendaylight.l2switch.hosttracker.plugin.inventory.Host;
 */
<<<<<<< HEAD
public class ConcurrentClusterAwareHostHashMap<K, V> implements
        ConcurrentMap<K, V> {
    private final OperationProcessor opProcessor;
    private final String topologyId;

    private static final Logger log = LoggerFactory
            .getLogger(ConcurrentClusterAwareHostHashMap.class);
=======
public class ConcurrentClusterAwareHostHashMap<K, V> implements ConcurrentMap<K, V> {
    private final OperationProcessor opProcessor;
    private final String topologyId;

    private static final Logger LOG = LoggerFactory.getLogger(ConcurrentClusterAwareHostHashMap.class);
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173

    /**
     * The instance identifiers for each host submitted to MD-SAL.
     */
    private final ConcurrentHashMap<InstanceIdentifier<Node>, K> instanceIDs;

    /**
     * The local Hosts' HashMap.
     */
    private final ConcurrentHashMap<K, V> hostHashMap;

<<<<<<< HEAD
    public ConcurrentClusterAwareHostHashMap(OperationProcessor opProcessor,
            String topologyId) {
=======
    public ConcurrentClusterAwareHostHashMap(OperationProcessor opProcessor, String topologyId) {
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
        this.opProcessor = opProcessor;
        this.topologyId = topologyId;
        this.hostHashMap = new ConcurrentHashMap<>();
        this.instanceIDs = new ConcurrentHashMap<>();
    }

    /**
<<<<<<< HEAD
     * Removes, if exists, the Host with the given InstanceIdentifier&lt;Node&gt; from
     * this local HashMap. Ideally used for host data listener events.
=======
     * Removes, if exists, the Host with the given
     * InstanceIdentifier&lt;Node&gt; from this local HashMap. Ideally used for
     * host data listener events.
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
     *
     * @param iiN
     *            the InstanceIdentifier&lt;Node&gt; of the Host to remove.
     * @return the removed Host if exits, null if it doesn't exist.
     */
    public synchronized V removeLocally(InstanceIdentifier<Node> iiN) {
        K hostId = this.instanceIDs.get(iiN);
        if (hostId != null) {
            this.instanceIDs.remove(iiN);
            return this.hostHashMap.remove(hostId);
        }
        return null;
    }

    /**
     * Removes, if exists, the Host with the given Key (HostId) from this local
     * HashMap. Ideally used for host data listener events.
     *
     * @param key
     *            the key (HostId) of the Host to remove.
     * @return the removed Host if exits, null if it doesn't exist.
     */
    public synchronized V removeLocally(K key) {
<<<<<<< HEAD
        Iterator<Entry<InstanceIdentifier<Node>, K>> iterator = this.instanceIDs
                .entrySet().iterator();
=======
        Iterator<Entry<InstanceIdentifier<Node>, K>> iterator = this.instanceIDs.entrySet().iterator();
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
        while (iterator.hasNext()) {
            if (iterator.next().getValue().equals(key)) {
                iterator.remove();
                break;
            }
        }
        return hostHashMap.remove(key);
    }

    /**
     * Puts the given value (Host) only in this local HashMap. Ideally used for
     * host data listener events.
     *
<<<<<<< HEAD
     * @param ii the value's (Host's) InstanceIdentifier&lt;Node&gt;
     * @param value the Host to store locally.
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>
     */
    public synchronized V putLocally(InstanceIdentifier<Node> ii, V value) {
        Host h = ((Host) value);
        log.trace("Putting locally {}", h.getId());
=======
     * @param ii
     *            the value's (Host's) InstanceIdentifier&lt;Node&gt;
     * @param value
     *            the Host to store locally.
     * @return the previous value associated with <tt>key</tt>, or <tt>null</tt>
     *         if there was no mapping for <tt>key</tt>
     */
    public synchronized V putLocally(InstanceIdentifier<Node> ii, V value) {
        Host h = ((Host) value);
        LOG.trace("Putting locally {}", h.getId());
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
        this.instanceIDs.put(ii, (K) h.getId());
        return this.hostHashMap.put((K) h.getId(), value);
    }

    /**
     * Removes the given hosts both locally and on MD-SAL database.
     *
     * @param hosts
     *            the hosts to remove.
     */
    public synchronized void removeAll(List<Host> hosts) {
<<<<<<< HEAD
        for (final Map.Entry<InstanceIdentifier<Node>, K> e : this.instanceIDs
                .entrySet()) {
            for (Host h : hosts) {
                if (e.getValue().equals(h.getId())) {
                    this.opProcessor
                            .enqueueOperation(new HostTrackerOperation() {
                                @Override
                                public void applyOperation(
                                        ReadWriteTransaction tx) {
                                    tx.delete(LogicalDatastoreType.OPERATIONAL,
                                            e.getKey());
                                }
                            });
=======
        for (final Map.Entry<InstanceIdentifier<Node>, K> e : this.instanceIDs.entrySet()) {
            for (Host h : hosts) {
                if (e.getValue().equals(h.getId())) {
                    this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                        @Override
                        public void applyOperation(ReadWriteTransaction tx) {
                            tx.delete(LogicalDatastoreType.OPERATIONAL, e.getKey());
                        }
                    });
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
                    this.hostHashMap.remove(e.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Forces the local Host with the given HostId to be merged into MD-SAL
     * database.
     *
     * @param hostid
     *            the Host's hostId that will be merged into MD-SAL database.
     */
    public synchronized void submit(HostId hostid) {
        Host h = (Host) this.hostHashMap.get(hostid);
        final Node hostNode = h.getHostNode();
<<<<<<< HEAD
        final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(
                hostNode.getKey(), topologyId);
        this.opProcessor.enqueueOperation(new HostTrackerOperation() {
            @Override
            public void applyOperation(ReadWriteTransaction tx) {
                tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID,
                        hostNode, true);
=======
        final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(hostNode.getKey(), topologyId);
        this.opProcessor.enqueueOperation(new HostTrackerOperation() {
            @Override
            public void applyOperation(ReadWriteTransaction tx) {
                tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID, hostNode, true);
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
            }
        });
        putLocally(buildNodeIID, (V) h);
        this.instanceIDs.put(buildNodeIID, (K) h.getId());
<<<<<<< HEAD
        log.trace("Enqueued for MD-SAL transaction {}", hostNode.getNodeId());
=======
        LOG.trace("Enqueued for MD-SAL transaction {}", hostNode.getNodeId());
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
    }

    /**
     * Puts all the given hosts into this local HashMap and into MD-SAL
     * database.
     *
     * @param hosts
     *            the hosts to be sent into MD-SAL database.
     */
    public synchronized void putAll(List<Host> hosts) {
        for (Host h : hosts) {
            final Node hostNode = h.getHostNode();
<<<<<<< HEAD
            final InstanceIdentifier<Node> buildNodeIID = Utilities
                    .buildNodeIID(hostNode.getKey(), topologyId);
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID,
                            hostNode, true);
=======
            final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(hostNode.getKey(), topologyId);
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID, hostNode, true);
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
                }
            });
            putLocally(buildNodeIID, (V) h);
            this.instanceIDs.put(buildNodeIID, (K) h.getId());
<<<<<<< HEAD
            log.trace("Putting MD-SAL {}", hostNode.getNodeId());
=======
            LOG.trace("Putting MD-SAL {}", hostNode.getNodeId());
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
        }
    }

    /**
     * Puts the given host in the this local HashMap and into MD-SAL database.
     *
     * @param hostId
     *            the key for the map
     * @param host
     *            the value for the map
     * @return the old value from the local cache if present, null otherwise.
     */
    @Override
    public synchronized V put(K hostId, V host) {
        final Node hostNode = ((Host) host).getHostNode();
<<<<<<< HEAD
        final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(
                hostNode.getKey(), topologyId);
        this.opProcessor.enqueueOperation(new HostTrackerOperation() {
            @Override
            public void applyOperation(ReadWriteTransaction tx) {
                tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID,
                        hostNode, true);
            }
        });
        log.trace("Putting MD-SAL {}", hostNode.getNodeId());
=======
        final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(hostNode.getKey(), topologyId);
        this.opProcessor.enqueueOperation(new HostTrackerOperation() {
            @Override
            public void applyOperation(ReadWriteTransaction tx) {
                tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID, hostNode, true);
            }
        });
        LOG.trace("Putting MD-SAL {}", hostNode.getNodeId());
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
        return putLocally(buildNodeIID, host);
    }

    /**
     * Removes the value (Host) with the given hostId from this local HashMap
     * and MD-SAL database.
     *
     * @param hostId
     *            the Host's hostId to remove
     * @return the old value from the local cache if present, null otherwise.
     */
    @Override
    public synchronized V remove(Object hostId) {
        V removedValue = this.hostHashMap.remove(hostId);
        if (removedValue != null) {
            Node hostNode = ((Host) removedValue).getHostNode();
<<<<<<< HEAD
            final InstanceIdentifier<Node> hnIID = Utilities.buildNodeIID(
                    hostNode.getKey(), topologyId);
=======
            final InstanceIdentifier<Node> hnIID = Utilities.buildNodeIID(hostNode.getKey(), topologyId);
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.delete(LogicalDatastoreType.OPERATIONAL, hnIID);
                }
            });
            this.instanceIDs.remove(hnIID);
        }
        return removedValue;
    }

    /**
     * If it's absent from the this local HashMap, puts the given host in the
     * this local HashMap and into MD-SAL database.
     *
     * @param key
     *            the key for the map
     * @param value
     *            the value for the map
     * @return the old value from the local cache if present, null otherwise.
     */
    @Override
    public synchronized V putIfAbsent(K key, V value) {
        if (!this.hostHashMap.contains(value)) {
            return this.hostHashMap.put(key, value);
        } else {
            return this.hostHashMap.get(key);
        }
    }

    /**
     * Removes the entry for a key only if currently mapped to a given value.
     *
     * @param key
     *            key with which the specified value is associated
     * @param value
     *            value expected to be associated with the specified key
     * @return <tt>true</tt> if the value was removed
     */
    @Override
    public synchronized boolean remove(Object key, Object value) {
<<<<<<< HEAD
        if (this.hostHashMap.containsKey((K) key)
                && this.hostHashMap.get((K) key).equals(value)) {
=======
        if (this.hostHashMap.containsKey((K) key) && this.hostHashMap.get((K) key).equals(value)) {
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
            remove((K) key);
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * Replaces the entry for a key only if currently mapped to a given value.
     *
     * @param key
     *            key with which the specified value is associated
     * @param oldValue
     *            value expected to be associated with the specified key
     * @param newValue
     *            value to be associated with the specified key
     */
    @Override
    public synchronized boolean replace(K key, V oldValue, V newValue) {
<<<<<<< HEAD
        if (this.hostHashMap.containsKey((K) key)
                && this.hostHashMap.get((K) key).equals(oldValue)) {
=======
        if (this.hostHashMap.containsKey((K) key) && this.hostHashMap.get((K) key).equals(oldValue)) {
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
            put(key, newValue);
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * Replaces the entry for a key only if currently mapped to some value.
     *
     * @param key
     *            key with which the specified value is associated
     * @param value
     *            value to be associated with the specified key
     * @return the previous value associated with the specified key, or
     *         <tt>null</tt> if there was no mapping for the key. (A
     *         <tt>null</tt> return can also indicate that the map previously
     *         associated <tt>null</tt> with the key, if the implementation
     *         supports null values.)
     */
    @Override
    public synchronized V replace(K key, V value) {
        if (this.hostHashMap.containsKey(key)) {
            return put(key, value);
        } else {
            return null;
        }
    }

    @Override
    public synchronized int size() {
        return this.hostHashMap.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return this.hostHashMap.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return this.hostHashMap.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return this.hostHashMap.contains(value);
    }

    @Override
    public synchronized V get(Object key) {
        return this.hostHashMap.get(key);
    }

    /**
     * Copies all of the mappings from the specified map to this local HashMap
     * and into MD-SAL.
     *
     * @param m
     *            mappings to be stored in this local HashMap and into MD-SAL
     */
    @Override
    public synchronized void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            final Node hostNode = ((Host) e.getValue()).getHostNode();
<<<<<<< HEAD
            final InstanceIdentifier<Node> buildNodeIID = Utilities
                    .buildNodeIID(hostNode.getKey(), topologyId);
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID,
                            hostNode, true);
=======
            final InstanceIdentifier<Node> buildNodeIID = Utilities.buildNodeIID(hostNode.getKey(), topologyId);
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.merge(LogicalDatastoreType.OPERATIONAL, buildNodeIID, hostNode, true);
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
                }
            });
            putLocally(buildNodeIID, e.getValue());
        }
    }

    /**
     *
     * Removes all of the mappings from this local HashMap and from MD-SAL. The
     * local HashMap will be empty after this call returns.
     *
     */
    @Override
    public synchronized void clear() {
<<<<<<< HEAD
        for (final Map.Entry<? extends InstanceIdentifier<Node>, ? extends K> e : this.instanceIDs
                .entrySet()) {
=======
        for (final Map.Entry<? extends InstanceIdentifier<Node>, ? extends K> e : this.instanceIDs.entrySet()) {
>>>>>>> 36e42ef84d5b4cf1662f9aa69be36545d3576173
            this.opProcessor.enqueueOperation(new HostTrackerOperation() {
                @Override
                public void applyOperation(ReadWriteTransaction tx) {
                    tx.delete(LogicalDatastoreType.OPERATIONAL, e.getKey());
                }
            });
        }
        this.hostHashMap.clear();
    }

    /**
     * Returns the KeySet from this local HashMap.
     *
     * @return the KeySet from this local HashMap.
     */
    @Override
    public synchronized Set<K> keySet() {
        return this.hostHashMap.keySet();
    }

    /**
     * Returns the Values from this local HashMap.
     *
     * @return the Values from this local HashMap.
     */
    @Override
    public synchronized Collection<V> values() {
        return this.hostHashMap.values();
    }

    /**
     * Returns the EntrySet from this local HashMap.
     *
     * @return the EntrySet from this local HashMap.
     */
    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return this.hostHashMap.entrySet();
    }

}
