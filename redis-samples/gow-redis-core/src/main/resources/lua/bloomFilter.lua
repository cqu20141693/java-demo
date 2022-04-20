for i,index in ipairs(ARGV) do
  local value = redis.call("GETBIT", KEYS[1],tonumber(index))
  if (value == nil or (value==0)) then
   return false;
  end
end
return true;