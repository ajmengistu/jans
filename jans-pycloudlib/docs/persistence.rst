Persistence
~~~~~~~~~~~

.. module:: jans.pycloudlib.persistence
.. autofunction:: render_salt
.. autofunction:: render_base_properties

LDAP
====

.. module:: jans.pycloudlib.persistence.ldap
.. autofunction:: render_ldap_properties
.. autofunction:: sync_ldap_truststore
.. autofunction:: extract_ldap_host
.. autofunction:: resolve_ldap_port

.. autoclass:: LdapClient
    :members:
    :private-members:
    :undoc-members:

Couchbase
=========

.. module:: jans.pycloudlib.persistence.couchbase
.. autofunction:: render_couchbase_properties
.. autofunction:: get_couchbase_user
.. autofunction:: get_couchbase_password
.. autofunction:: get_couchbase_superuser
.. autofunction:: get_couchbase_superuser_password
.. autofunction:: get_couchbase_mappings
.. autofunction:: get_couchbase_conn_timeout
.. autofunction:: get_couchbase_conn_max_wait
.. autofunction:: get_couchbase_scan_consistency
.. autofunction:: get_couchbase_keepalive_timeout
.. autofunction:: get_couchbase_keepalive_interval
.. autofunction:: sync_couchbase_truststore
.. autofunction:: build_n1ql_request_body

.. autoclass:: CouchbaseClient
    :members:
    :private-members:
    :undoc-members:

.. note::

    The following classes are adapters to interact with Couchbase REST and N1QL APIs.
    Users **SHOULD NOT** use any of this class directly, use :class:`~jans.pycloudlib.persistence.couchbase.CouchbaseClient` instead.

.. autoclass:: BaseApiClient
    :members:
    :private-members:
    :undoc-members:

.. autoclass:: RestApiClient
    :members:
    :private-members:
    :undoc-members:

.. autoclass:: N1qlApiClient
    :members:
    :private-members:
    :undoc-members:

.. note::

    The following classes, methods, or functions are deprecated
    and will be removed in future releases.

.. autoclass:: BaseClient
.. autoclass:: RestClient
.. autoclass:: N1qlClient

Hybrid
======

.. module:: jans.pycloudlib.persistence.hybrid
.. autofunction:: render_hybrid_properties

SQL
===

.. module:: jans.pycloudlib.persistence.sql
.. autofunction:: render_sql_properties
.. autofunction:: get_sql_password

.. autoclass:: SQLClient
    :members:
    :private-members:
    :undoc-members:

.. note::

    The following classes are adapters to interact with SQL databases in low-level API.
    Users **SHOULD NOT** use any of this class directly, use :class:`~jans.pycloudlib.persistence.sql.SQLClient` instead.

.. autoclass:: BaseAdapter
    :members:
    :private-members:
    :undoc-members:

.. autoclass:: MysqlAdapter
    :members:
    :private-members:
    :undoc-members:

.. autoclass:: PostgresqlAdapter
    :members:
    :private-members:
    :undoc-members:

.. note::

    The following classes, methods, or functions are deprecated
    and will be removed in future releases.

.. autoclass:: BaseClient
.. autoclass:: MysqlClient
.. autoclass:: PostgresqlClient

Spanner
=======

.. module:: jans.pycloudlib.persistence.spanner
.. autofunction:: render_spanner_properties

.. autoclass:: SpannerClient
    :members:
    :private-members:
    :undoc-members:
