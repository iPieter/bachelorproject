yaw = imu.gyro.yaw;
steps = 1:100:length(yaw);
ni = length(steps);
o = [];
biggest = -1000000000000000;
biggestIndex = 0;
for j = 1:ni
    i = steps(j);
    left = 10;
    if( i + left > length(yaw) )
       left = length(yaw)-i; 
    end
    [a,index] = max( abs(yaw(i:i+left)) );
    o = [o yaw(i+index) ];
end