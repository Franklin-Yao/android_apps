<?php

    /*
	|--------------------------------------------------------------------------
	| Database Connections for SAE
	|--------------------------------------------------------------------------
	|
	| Here are each of the database connections setup for your application.
	| Of course, examples of configuring each database platform that is
	| supported by Laravel is shown below to make development simple.
	|
	|
	| All database work in Laravel is done through the PHP PDO facilities
	| so make sure you have the driver for your particular database of
	| choice installed on your machine before you begin development.
	|
	*/
return array(
    'connections' => array(

		'mysql' => array(
			'driver'    => 'mysql',
            'host'      => SAE_MYSQL_HOST_M,
            'port'      => SAE_MYSQL_PORT,
            'database'  => SAE_MYSQL_DB,
            'username'  => SAE_MYSQL_USER,
            'password'  => SAE_MYSQL_PASS,
            'charset'   => 'utf8',
            'collation' => 'utf8_general_ci',
            'prefix'    => '',
		),
	),
);