Wait
~~~~

.. module:: jans.pycloudlib.wait
.. autofunction:: wait_for

.. note::
    Most of the time, users may only need to run ``wait_for`` function to add readiness check. If somehow this function is not sufficient, low-level functions ``wait_for_*`` are available.

.. autodata:: retry_on_exception
.. autofunction:: wait_for_config
.. autofunction:: wait_for_secret
.. autofunction:: wait_for_ldap
.. autofunction:: wait_for_ldap_conn
.. autofunction:: wait_for_couchbase
.. autofunction:: wait_for_couchbase_conn
.. autofunction:: wait_for_sql
.. autofunction:: wait_for_sql_conn
.. autofunction:: wait_for_spanner
.. autofunction:: wait_for_spanner_conn
.. autofunction:: get_wait_max_time
.. autofunction:: get_wait_interval
.. autoexception:: WaitError
