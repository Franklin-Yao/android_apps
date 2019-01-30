<?php

    /*
	|--------------------------------------------------------------------------
	| SAE Wrapper Prefix
	|--------------------------------------------------------------------------
	|
	| This prefix stand for SAE wrapper. Using this prefix, we can access storage,
	| memcached, kvdb of SAE by simply keeping 'drive' of cache, session 'file'.
	|
	| Supported:
    |	        "saekv://"          (recommended for string),
    |           "saemc://"          (fastest but most expensive),
    |           "saestor://domain"  (suitable for resource)
	|
	*/

return array(
    'wrapper' => 'saekv://',
);