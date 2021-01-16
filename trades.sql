truncate table alloc;
truncate table trade;
truncate table parameter;

insert into parameter values ('lastDate','01/01/2020');
commit;

select * from trade;
select * from parameter;

